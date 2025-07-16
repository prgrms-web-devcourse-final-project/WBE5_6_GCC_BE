package com.honlife.core.app.model.routine.service;

import com.honlife.core.app.controller.routine.payload.RoutineDetailResponse;
import com.honlife.core.app.controller.routine.payload.RoutineSaveRequest;
import com.honlife.core.app.controller.routine.payload.RoutinesDailyResponse;
import com.honlife.core.app.controller.routine.payload.RoutinesResponse;
import com.honlife.core.app.model.routine.code.RepeatType;
import com.honlife.core.app.model.routine.dto.RoutineItemDTO;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
    /**
     * 혹시 이거 다 지워도 되나요?
     */
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
     * 사용자 일주일 루틴 조회 입니다
     * return RoutinesResponse
     * 지연로딩으로 routine 들고올때 category와 fetch join사용 했습니다
     * 스케줄러에 없을시 날짜 계산을 해서 루틴들고오는거를 만들었습니다
     */
  public RoutinesResponse getUserWeeklyRoutines(String userEmail) {

      Member member = memberRepository.findByEmail(userEmail)
          .orElseThrow(() -> new EntityNotFoundException("해당 아이디가 존재하지 않습니다"));

      List<Routine> routines = routineRepository.findAllByMemberWithCategory(member);

      //해당날짜에서 일주일치 계산
      LocalDate startDate = LocalDate.now();
      LocalDate endDate = startDate.plusDays(6);

      Map<LocalDate, List<RoutineItemDTO>> groupedByDate = routines.stream()
          .flatMap(routine ->
              startDate.datesUntil(endDate.plusDays(1))
                  .filter(currentDate -> routine.getRepeatType().isMatched(currentDate, routine.getRepeatValue()))
                  .map(currentDate -> {
                    /**부모 카테고리가 null인경우를 검증하여서 null이 아닌경우는 부모 카테고리도 들고오는 로직 */
                      Category parentCategory = null;
                      Long parentId = routine.getCategory().getParentId();
                      if (parentId != null) {
                          parentCategory = categoryRepository.findById(parentId)
                              .orElse(null);
                      }

                      RoutineSchedule routineSchedule = routineScheduleRepository
                          .findByRoutineIdAndDate(routine.getId(), currentDate);

                      return RoutineItemDTO.builder()
                          .scheduleId(routineSchedule != null ? routineSchedule.getId() : null)
                          .routineId(routine.getId())
                          .majorCategory(parentCategory != null ? parentCategory.getName() : routine.getCategory().getName())
                          .subCategory(parentCategory != null ? routine.getCategory().getName() : null)
                          .name(routine.getContent())
                          .triggerTime(routine.getTriggerTime())
                          .isDone(routineSchedule != null ? routineSchedule.getIsDone() : false)
                          .isImportant(routine.getIsImportant())
                          .date(currentDate)
                          .build();
                  })
          )
          .collect(Collectors.groupingBy(RoutineItemDTO::getDate));


      RoutinesResponse response = new RoutinesResponse();
      response.setRoutines(groupedByDate);

      return response;
  }

  /**
   * 사용자 당일 루틴 조회 입니다
   * return RoutinesDailyResponse
   * 지연로딩으로 routine 들고올때 category와 fetch join사용 했습니다
   * 스케줄러에 없을시 날짜 계산을 해서 루틴들고오는거를 만들었습니다
   */
    public RoutinesDailyResponse getDailyRoutines(String userEmail) {
        Member member = memberRepository.findByEmail(userEmail)
            .orElseThrow(() -> new EntityNotFoundException("해당 아이디가 존재하지 않습니다"));

        List<Routine> routines = routineRepository.findAllByMemberWithCategory(member);

        List<RoutineItemDTO> responseRoutines = routines.stream()

            .filter(routine -> routine.getRepeatType().isMatched(LocalDate.now(), routine.getRepeatValue()))
            .map(routine -> {
                Category parentCategory = null;
                Long parentId = routine.getCategory().getParentId();
                if (parentId != null) {
                    parentCategory = categoryRepository.findById(parentId)
                        .orElseThrow(() -> new RuntimeException("부모 카테고리 없음"));
                }

                RoutineSchedule routineSchedule = routineScheduleRepository
                    .findByRoutineIdAndDate(routine.getId(), LocalDate.now());


                return RoutineItemDTO.builder()
                    .scheduleId(routineSchedule != null ? routineSchedule.getId() : null)
                    .routineId(routine.getId())
                    .majorCategory(parentCategory != null ? parentCategory.getName() : routine.getCategory().getName())
                    .subCategory(parentCategory != null ? routine.getCategory().getName() : null)
                    .name(routine.getContent())
                    .triggerTime(routine.getTriggerTime())
                    .isDone(routineSchedule != null ? routineSchedule.getIsDone() : false)
                    .isImportant(routine.getIsImportant())
                    .date(LocalDate.now())
                    .build();
            })
            .toList();

        RoutinesDailyResponse response = new RoutinesDailyResponse();
        response.setDate(LocalDate.now());
        response.setRoutines(responseRoutines);

        return response;
    }

  /**
   * 사용자  루틴 단건 조회 입니다
   * return void
   */
    @Transactional
    public void createRoutine(RoutineSaveRequest routineSaveRequest, String userId) {

      Member member = memberRepository.findByEmail(userId)
          .orElseThrow(()-> new EntityNotFoundException("멤버 엔티티가 존재하지 않습니다"));

      Category category = categoryRepository.findById(routineSaveRequest.getCategoryId())
          .orElseThrow(() -> new EntityNotFoundException("카테고리 입력은 필수 입니다"));

      Routine routine = Routine.builder()
          .category(category)
          .content(routineSaveRequest.getContent())
          .triggerTime(routineSaveRequest.getTriggerTime())
          .isImportant(routineSaveRequest.getIsImportant())
          .repeatType(routineSaveRequest.getRepeatType())
          .repeatValue(routineSaveRequest.getRepeatValue())
          .member(member)
          .build();

      routineRepository.save(routine);


    }


    @Transactional
    public void updateRoutine(Long routineId, RoutineSaveRequest request, String userId) {
      Member member = memberRepository.findByEmail(userId)
            .orElseThrow(()-> new EntityNotFoundException("멤버 엔티티가 존재하지 않습니다"));

      Routine routine = routineRepository.findById(routineId)
          .orElseThrow(() -> new EntityNotFoundException("해당 루틴이 존재하지 않습니다"));

      Category category = categoryRepository.findById(request.getCategoryId())
          .orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다"));

        routine.updateRoutine(category, request.getContent(), request.getTriggerTime(),
            request.getIsImportant(), request.getRepeatType(), request.getRepeatValue(),member);
    }

  public RoutineDetailResponse getDetailRoutine(Long routineId) {

    Routine routine = routineRepository.findByIdWithCategory(routineId)
        .orElseThrow(() -> new EntityNotFoundException("해당 루틴이 존재하지 않습니다"));

    Category parentCategory = null;
    Long parentId = routine.getCategory().getParentId();
    if (parentId != null) {
      parentCategory = categoryRepository.findById(parentId)
          .orElse(null);
    }


    RoutineDetailResponse response = RoutineDetailResponse.builder()
        .routineId(routineId)
        .categoryId(routine.getCategory().getId())
        .majorCategory(parentCategory != null ? parentCategory.getName() : routine.getCategory().getName())
        .subCategory(parentCategory != null ? routine.getCategory().getName() : null)
        .name(routine.getContent())
        .triggerTime(routine.getTriggerTime())
        .isImportant(routine.getIsImportant())
        .repeatType(routine.getRepeatType())
        .repeatValue(routine.getRepeatValue())
        .build();

    return response;
  }
}
