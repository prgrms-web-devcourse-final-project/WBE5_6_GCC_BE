package spring.grepp.honlife.routine.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.category.domain.Category;
import spring.grepp.honlife.category.repos.CategoryRepository;
import spring.grepp.honlife.member.domain.Member;
import spring.grepp.honlife.member.repos.MemberRepository;
import spring.grepp.honlife.routine.domain.Routine;
import spring.grepp.honlife.routine.model.RoutineDTO;
import spring.grepp.honlife.routine.repos.RoutineRepository;
import spring.grepp.honlife.util.NotFoundException;


@Service
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    public RoutineService(final RoutineRepository routineRepository,
            final MemberRepository memberRepository, final CategoryRepository categoryRepository) {
        this.routineRepository = routineRepository;
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<RoutineDTO> findAll() {
        final List<Routine> routines = routineRepository.findAll(Sort.by("id"));
        return routines.stream()
                .map(routine -> mapToDTO(routine, new RoutineDTO()))
                .toList();
    }

    public RoutineDTO get(final Integer id) {
        return routineRepository.findById(id)
                .map(routine -> mapToDTO(routine, new RoutineDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final RoutineDTO routineDTO) {
        final Routine routine = new Routine();
        mapToEntity(routineDTO, routine);
        return routineRepository.save(routine).getId();
    }

    public void update(final Integer id, final RoutineDTO routineDTO) {
        final Routine routine = routineRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(routineDTO, routine);
        routineRepository.save(routine);
    }

    public void delete(final Integer id) {
        routineRepository.deleteById(id);
    }

    private RoutineDTO mapToDTO(final Routine routine, final RoutineDTO routineDTO) {
        routineDTO.setCreatedAt(routine.getCreatedAt());
        routineDTO.setUpdatedAt(routine.getUpdatedAt());
        routineDTO.setIsActive(routine.getIsActive());
        routineDTO.setId(routine.getId());
        routineDTO.setContent(routine.getContent());
        routineDTO.setDate(routine.getDate());
        routineDTO.setTriggerTime(routine.getTriggerTime());
        routineDTO.setIsDone(routine.getIsDone());
        routineDTO.setMember(routine.getMember() == null ? null : routine.getMember().getId());
        routineDTO.setCategory(routine.getCategory() == null ? null : routine.getCategory().getId());
        return routineDTO;
    }

    private Routine mapToEntity(final RoutineDTO routineDTO, final Routine routine) {
        routine.setCreatedAt(routineDTO.getCreatedAt());
        routine.setUpdatedAt(routineDTO.getUpdatedAt());
        routine.setIsActive(routineDTO.getIsActive());
        routine.setContent(routineDTO.getContent());
        routine.setDate(routineDTO.getDate());
        routine.setTriggerTime(routineDTO.getTriggerTime());
        routine.setIsDone(routineDTO.getIsDone());
        final Member member = routineDTO.getMember() == null ? null : memberRepository.findById(routineDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        routine.setMember(member);
        final Category category = routineDTO.getCategory() == null ? null : categoryRepository.findById(routineDTO.getCategory())
                .orElseThrow(() -> new NotFoundException("category not found"));
        routine.setCategory(category);
        return routine;
    }

}
