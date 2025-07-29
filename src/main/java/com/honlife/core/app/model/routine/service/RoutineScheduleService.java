package com.honlife.core.app.model.routine.service;

import com.honlife.core.app.controller.routine.payload.RoutineScheduleCompleteRequest;
import com.honlife.core.app.model.member.service.MemberPointService;
import com.honlife.core.app.model.point.code.PointSourceType;
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
        if(routineSchedule.getIsDone().equals(false)){
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
                && ChronoUnit.WEEKS.between(routine.getInitDate(), today) % routine.getRepeatTerm() == 0) {

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
}
