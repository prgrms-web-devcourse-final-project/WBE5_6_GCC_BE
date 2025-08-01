package com.honlife.core.app.model.routine.service;

import com.honlife.core.app.controller.routine.payload.RoutineScheduleCompleteRequest;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.member.service.MemberPointService;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.app.model.point.code.PointSourceType;
import com.honlife.core.app.model.routine.code.RepeatType;
import com.honlife.core.app.model.routine.dto.RoutineScheduleInfo;
import com.honlife.core.app.model.routine.dto.RoutineSummaryDTO;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.event.CommonEvent;
import com.honlife.core.infra.response.ResponseCode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.honlife.core.app.model.routine.repos.RoutineRepository;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoutineScheduleService {

    private final RoutineScheduleRepository routineScheduleRepository;
    private final RoutineRepository routineRepository;
    private final MemberPointService memberPointService;
    private final ApplicationEventPublisher eventPublisher;


    /** 루틴중에서 완료처리와 완료한거 취소하는 처리 포인트 처리
     * 아직 포인트 업적 수령 pr이 머지가 안되서 머지되면 반영해서 다시 작성하겠습니다 */

    @Async
    @Transactional
    public void completeRoutineSchedule(Long scheduleId, RoutineScheduleCompleteRequest request, String userEmail) {

        // 실제 구현 시에는 다음과 같은 로직 수행:
        // 1. scheduleId로 RoutineSchedule 조회
        // 2. 해당 스케줄이 현재 사용자의 것인지 검증
        // 3. isDone 상태 업데이트
        // 4. 완료 시 포인트 지급 로직 (중복 지급 방지)
        // 5. 취소 시 포인트 회수 로직 (필요한 경우)

        RoutineSchedule routineSchedule = routineScheduleRepository.findWithRoutineAndMemberById(scheduleId);

        if(routineSchedule == null){
           throw new CommonException(ResponseCode.NOT_FOUND_ROUTINE);
        }

        if(!routineSchedule.getRoutine().getMember().getEmail().equals(userEmail)){
            throw new CommonException(ResponseCode.NOT_FOUND_MEMBER);
        }
        /** false면 취소 버튼을 누른거므로 바꾸고 포인트 회수**/
        if(request.getIsDone().equals(false)){
            routineSchedule.setIsDone(request.getIsDone());

            memberPointService.subtractPoint(userEmail, null, PointSourceType.ROUTINE );


        }else{
            /** true라구 하고 포인트 적립한다 ~ */
            routineSchedule.setIsDone(request.getIsDone());

            memberPointService.addPoint(userEmail, null, PointSourceType.ROUTINE );


        }

        // 루틴 완료 이벤트 발행
        eventPublisher.publishEvent(
            CommonEvent.builder()
                .memberEmail(userEmail)
                .routineScheduleId(scheduleId)
                .routineId(routineSchedule.getRoutine().getId())
                .isDone(request.getIsDone())
                .build()
        );
    }

    /** DB에 해당 루틴에 관련된 거 DB에 저장하는 로직 구현*/
    @Async
    @Transactional
    public void createSchedule(LocalDate today) {

        List<Routine> routines = routineRepository.findAllByIsActiveTrue();


        for (Routine routine : routines) {
            if (!today.isBefore(routine.getInitDate())
                && routine.getRepeatType().isMatched(today, routine.getRepeatValue())
                && isToday(routine, today)) {

                log.info("✅ [Scheduler] 루틴 ID {} 에 대한 스케줄 생성", routine.getId());

                boolean exists = routineScheduleRepository.existsByRoutineAndScheduledDate(routine, today);
                if (!exists) {
                    RoutineSchedule schedule = RoutineSchedule.builder()
                        .scheduledDate(today)
                        .isDone(false)
                        .routine(routine)
                        .build();
                    routineScheduleRepository.save(schedule);

                    log.info("[Scheduler] 루틴 ID {} 의 {} 스케줄 저장 완료", routine.getId() , today);
                }
            }
        }

    }

    public List<RoutineSummaryDTO> getRoutineSummary(String userEmail, LocalDate startDate,
        LocalDate endDate) {

        List<RoutineSchedule> routineSchedules = routineScheduleRepository.findAllByDateBetween(userEmail, startDate, endDate);

        return routineSchedules.stream().map(routineSchedule -> {
            return RoutineSummaryDTO.builder()
                .scheduledDate(routineSchedule.getScheduledDate())
                .routineContent(routineSchedule.getRoutine().getContent())
                .categoryName(routineSchedule.getRoutine().getCategory().getName())
                .isDone(routineSchedule.getIsDone())
                .build();
        }).toList();

    }

    /**
     * 배지 진행률 업데이트를 위한 루틴 스케줄 정보 조회
     *
     * @param scheduleId 루틴 스케줄 ID
     * @return 루틴 스케줄 정보 (memberId, categoryId 포함) 또는 null
     */
    @Transactional(readOnly = true)
    public RoutineScheduleInfo getRoutineScheduleInfoForBadge(Long scheduleId) {
        RoutineSchedule schedule = routineScheduleRepository.findWithRoutineAndMemberById(scheduleId);

        if (schedule == null) {
            log.warn("RoutineSchedule not found for badge update - scheduleId: {}", scheduleId);
            return null;
        }

        return RoutineScheduleInfo.builder()
            .memberId(schedule.getRoutine().getMember().getId())
            .categoryId(schedule.getRoutine().getCategory().getId())
            .build();
    }

    /**
     * term 을 확인해서 오늘이 루틴을 행하는 날인지 확인합니다.
     * @param routine 루틴 정보
     * @return Boolean
     */
    // todo routineService에도 똑같은 코드가 있으나 해당 메소드를 사용하기엔 순환참조 문제가 발생할 거 같아 일단 임시로 여기에 생성 추후 리팩토링 필요
    private boolean isToday(Routine routine, LocalDate date) {
        if(routine.getRepeatType() == RepeatType.DAILY)
            return ChronoUnit.DAYS.between(routine.getInitDate(), date) % routine.getRepeatTerm() == 0;
        else if(routine.getRepeatType() == RepeatType.WEEKLY)
            return ChronoUnit.WEEKS.between(routine.getInitDate(), date) % routine.getRepeatTerm() == 0;
        else if(routine.getRepeatType() == RepeatType.MONTHLY)
            return ChronoUnit.MONTHS.between(routine.getInitDate(), date) % routine.getRepeatTerm() == 0;
        else{
            return false;
        }
    }

    /**
     * 로그인 시 루틴 스케줄을 생성합니다.
     * @param memberEmail
     * @param today
     */
    public void createRoutineScheduleForMember(String memberEmail, LocalDate today) {
        // 사용자의 루틴을 전부 불러오기
        List<Routine> routines = routineRepository.findAllByMember_EmailAndIsActive(memberEmail, true);

        for (Routine routine : routines) {

            if (!today.isBefore(routine.getInitDate())
                && routine.getRepeatType().isMatched(today, routine.getRepeatValue())
                && isToday(routine, today)) {

                boolean exists = routineScheduleRepository.existsByRoutineAndScheduledDate(routine, today);
                if (!exists) {
                    RoutineSchedule schedule = RoutineSchedule.builder()
                        .scheduledDate(today)
                        .isDone(false)
                        .routine(routine)
                        .build();
                    routineScheduleRepository.save(schedule);
                }
            }
        }
    }
}
