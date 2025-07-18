package com.honlife.core.app.model.routine.service;

import com.honlife.core.app.controller.routine.payload.RoutineScheduleCompleteRequest;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberPoint;
import com.honlife.core.app.model.member.repos.MemberPointRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.point.domain.PointPolicy;
import com.honlife.core.app.model.point.repos.PointPolicyRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.honlife.core.app.model.routine.dto.RoutineScheduleDTO;
import com.honlife.core.app.model.routine.repos.RoutineRepository;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RoutineScheduleService {

    private final RoutineScheduleRepository routineScheduleRepository;
    private final RoutineRepository routineRepository;
    private final PointPolicyRepository pointPolicyRepository;
    private final MemberRepository memberRepository;
    private final MemberPointRepository memberPointRepository;



    public List<RoutineScheduleDTO> findAll() {
        final List<RoutineSchedule> routineSchedules = routineScheduleRepository.findAll(Sort.by("id"));
        return routineSchedules.stream()
                .map(routineSchedule -> mapToDTO(routineSchedule, new RoutineScheduleDTO()))
                .toList();
    }

    public RoutineScheduleDTO get(final Long id) {
        return routineScheduleRepository.findById(id)
                .map(routineSchedule -> mapToDTO(routineSchedule, new RoutineScheduleDTO()))
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ROUTINE));
    }

    public Long create(final RoutineScheduleDTO routineScheduleDTO) {
        final RoutineSchedule routineSchedule = new RoutineSchedule();
        mapToEntity(routineScheduleDTO, routineSchedule);
        return routineScheduleRepository.save(routineSchedule).getId();
    }

    public void update(final Long id, final RoutineScheduleDTO routineScheduleDTO) {
        final RoutineSchedule routineSchedule = routineScheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ROUTINE));
        mapToEntity(routineScheduleDTO, routineSchedule);
        routineScheduleRepository.save(routineSchedule);
    }

    public void delete(final Long id) {
        routineScheduleRepository.deleteById(id);
    }

    private RoutineScheduleDTO mapToDTO(final RoutineSchedule routineSchedule,
            final RoutineScheduleDTO routineScheduleDTO) {
        routineScheduleDTO.setId(routineSchedule.getId());
        routineScheduleDTO.setDate(routineSchedule.getDate());
        routineScheduleDTO.setIsDone(routineSchedule.getIsDone());
        routineScheduleDTO.setCreatedAt(routineSchedule.getCreatedAt());
        routineScheduleDTO.setRoutine(routineSchedule.getRoutine() == null ? null : routineSchedule.getRoutine().getId());
        return routineScheduleDTO;
    }

    private RoutineSchedule mapToEntity(final RoutineScheduleDTO routineScheduleDTO,
            final RoutineSchedule routineSchedule) {
        routineSchedule.setDate(routineScheduleDTO.getDate());
        routineSchedule.setIsDone(routineScheduleDTO.getIsDone());
        routineSchedule.setCreatedAt(routineScheduleDTO.getCreatedAt());
        final Routine routine = routineScheduleDTO.getRoutine() == null ? null : routineRepository.findById(routineScheduleDTO.getRoutine())
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ROUTINE));
        routineSchedule.setRoutine(routine);
        return routineSchedule;
    }
    /** 루틴중에서 완료처리와 완료한거 취소하는 처리 포인트 처리 */

    @Transactional
    public void completeRoutineSchedule(Long scheduleId, RoutineScheduleCompleteRequest request, String userEmail) {

        // 실제 구현 시에는 다음과 같은 로직 수행:
        // 1. scheduleId로 RoutineSchedule 조회
        // 2. 해당 스케줄이 현재 사용자의 것인지 검증
        // 3. isDone 상태 업데이트
        // 4. 완료 시 포인트 지급 로직 (중복 지급 방지)
        // 5. 취소 시 포인트 회수 로직 (필요한 경우)

        RoutineSchedule routineSchedule = routineScheduleRepository.findWithRoutineAndMemberById(scheduleId);
        Member member = memberRepository.findByEmail(userEmail)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_MEMBER));

        MemberPoint memberPoint = memberPointRepository.findByMember(member);

        if(routineSchedule == null){
           throw new CommonException(ResponseCode.NOT_FOUND_ROUTINE);
        }

        if(!routineSchedule.getRoutine().getMember().getEmail().equals(userEmail)){
            throw new CommonException(ResponseCode.NOT_FOUND_MEMBER);
        }
        /** false면 취소 버튼을 누른거므로 바꾸고 포인트 회수**/
        if(routineSchedule.getIsDone().equals(false)){
            routineSchedule.setIsDone(request.getIsDone());

            // 포인트 회수 로직
            PointPolicy policy = pointPolicyRepository.findByType("ROUTINE");
            int reversePoint = policy.getPoint();

            int point = memberPoint.getPoint() - reversePoint;
            memberPoint.setPoint(point);


        }else{
            /** true라구 하고 포인트 적립한다 ~ */
            routineSchedule.setIsDone(request.getIsDone());

            // 포인트 추가 로직
            PointPolicy policy = pointPolicyRepository.findByType("ROUTINE");
            int addPoint = policy.getPoint();

            int point = memberPoint.getPoint() + addPoint;
            memberPoint.setPoint(point);
        }

        /** 루틴 완료시 이제 완료율로 뱃지 체크  어떤 뱃지 할건지 논의 해봐야함 */


    }
}
