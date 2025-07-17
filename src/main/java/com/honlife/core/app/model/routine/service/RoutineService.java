package com.honlife.core.app.model.routine.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.honlife.core.app.model.routine.dto.RoutineDTO;
import com.honlife.core.app.model.routine.repos.RoutineRepository;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
import com.honlife.core.infra.util.NotFoundException;
import com.honlife.core.infra.util.ReferencedWarning;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final RoutineScheduleRepository routineScheduleRepository;

    public RoutineService(final RoutineRepository routineRepository,
            final MemberRepository memberRepository, final CategoryRepository categoryRepository,
            final RoutineScheduleRepository routineScheduleRepository) {
        this.routineRepository = routineRepository;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.routineScheduleRepository = routineScheduleRepository;
    }

    public List<RoutineDTO> findAll() {
        final List<Routine> routines = routineRepository.findAll(Sort.by("id"));
        return routines.stream()
                .map(routine -> mapToDTO(routine, new RoutineDTO()))
                .toList();
    }

    public RoutineDTO get(final Long id) {
        return routineRepository.findById(id)
                .map(routine -> mapToDTO(routine, new RoutineDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RoutineDTO routineDTO) {
        final Routine routine = new Routine();
        mapToEntity(routineDTO, routine);
        return routineRepository.save(routine).getId();
    }

    public void update(final Long id, final RoutineDTO routineDTO) {
        final Routine routine = routineRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(routineDTO, routine);
        routineRepository.save(routine);
    }

    public void delete(final Long id) {
        routineRepository.deleteById(id);
    }

    private RoutineDTO mapToDTO(final Routine routine, final RoutineDTO routineDTO) {
        routineDTO.setCreatedAt(routine.getCreatedAt());
        routineDTO.setUpdatedAt(routine.getUpdatedAt());
        routineDTO.setIsActive(routine.getIsActive());
        routineDTO.setRoutineId(routine.getId());
        routineDTO.setContent(routine.getContent());
        routineDTO.setTriggerTime(routine.getTriggerTime());
        routineDTO.setIsImportant(routine.getIsImportant());
        routineDTO.setRepeatType(routine.getRepeatType());
        routineDTO.setRepeatValue(routine.getRepeatValue());
        routineDTO.setMember(routine.getMember() == null ? null : routine.getMember().getId());
        routineDTO.setCategory(routine.getCategory() == null ? null : routine.getCategory().getId());
        return routineDTO;
    }

    private Routine mapToEntity(final RoutineDTO routineDTO, final Routine routine) {
        routine.setCreatedAt(routineDTO.getCreatedAt());
        routine.setUpdatedAt(routineDTO.getUpdatedAt());
        routine.setIsActive(routineDTO.getIsActive());
        routine.setContent(routineDTO.getContent());
        routine.setTriggerTime(routineDTO.getTriggerTime());
        routine.setIsImportant(routineDTO.getIsImportant());
        routine.setRepeatType(routineDTO.getRepeatType());
        routine.setRepeatValue(routineDTO.getRepeatValue());
        final Member member = routineDTO.getMember() == null ? null : memberRepository.findById(routineDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        routine.setMember(member);
        final Category category = routineDTO.getCategory() == null ? null : categoryRepository.findById(routineDTO.getCategory())
                .orElseThrow(() -> new NotFoundException("category not found"));
        routine.setCategory(category);
        return routine;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Routine routine = routineRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final RoutineSchedule routineRoutineSchedule = routineScheduleRepository.findFirstByRoutine(routine);
        if (routineRoutineSchedule != null) {
            referencedWarning.setKey("routine.routineSchedule.routine.referenced");
            referencedWarning.addParam(routineRoutineSchedule.getId());
            return referencedWarning;
        }
        return null;
    }

    /**
     * 멤버 아이디를 통해 조회하여 연관된 모든 루틴을 삭제합니다.
     * @param memberId 멤버 식별아이디
     */
    @Transactional
    public void softDropRoutineByMemberId(Long memberId) {
        routineRepository.softDropByMemberId(memberId);
    }

    /**
     * 멤버와 연관된 루틴 중 활성화된 루틴중 가장 첫번째 것을 조회합니다.
     * @param member 멤버
     * @param isActive 활성화 상태
     * @return {@link Routine}
     */
    public Routine findFirstRoutineByMemberAndIsActive(Member member, boolean isActive) {

        return routineRepository.findFirstByMemberAndIsActive(member, isActive);
    }
}
