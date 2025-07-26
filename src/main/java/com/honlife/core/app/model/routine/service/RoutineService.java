package com.honlife.core.app.model.routine.service;

import com.honlife.core.app.controller.routine.payload.RoutineUpdateRequest;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.routine.code.RepeatType;
import com.honlife.core.app.model.routine.dto.RoutineTodayItemDTO;
import com.honlife.core.infra.response.ResponseCode;
import com.honlife.core.app.controller.routine.payload.RoutineSaveRequest;
import com.honlife.core.app.model.routine.dto.RoutineDetailDTO;
import com.honlife.core.app.model.routine.dto.RoutineItemDTO;
import com.honlife.core.infra.error.exceptions.CommonException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
import com.honlife.core.infra.error.exceptions.NotFoundException;
import com.honlife.core.infra.error.exceptions.ReferencedWarning;
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
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ROUTINE));
    }

    public Long create(final RoutineDTO routineDTO) {
        final Routine routine = new Routine();
        mapToEntity(routineDTO, routine);
        return routineRepository.save(routine).getId();
    }

    public void update(final Long id, final RoutineDTO routineDTO) {
        final Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ROUTINE));
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
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));
        routine.setMember(member);
        final Category category = routineDTO.getCategory() == null ? null : categoryRepository.findById(routineDTO.getCategory())
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_CATEGORY));
        routine.setCategory(category);
        return routine;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_ROUTINE));
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
  public Map<LocalDate, List<RoutineItemDTO>> getUserWeeklyRoutines(String userEmail, LocalDate date) {

    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_MEMBER));

    List<Routine> routines = routineRepository.findAllByMemberAndIsActiveWithCategory(member, true);


    //해당날짜에서 일주일치 계산
    LocalDate startDate = date.with(DayOfWeek.MONDAY);
    LocalDate endDate = date.with(DayOfWeek.SUNDAY);

    Map<LocalDate, List<RoutineItemDTO>> groupedByDate = routines.stream()
        .flatMap(routine ->
            startDate.datesUntil(endDate.plusDays(1))
                .filter(currentDate ->
                    //해당 날짜로부터 시작 날짜 포함해서 이후에 값만 들고오는거 입니다
                    !routine.getStartDate().isAfter(LocalDate.now()) &&
                        routine.getRepeatType().isMatched(currentDate, routine.getRepeatValue()) &&
                        // 달마다 차이나는 것을 무시하고 순수한 주차 사이 값을 돌려준다는 메서드인데 진짜 너무 대박이네요
                        ChronoUnit.WEEKS.between(routine.getStartDate(), currentDate) % routine.getRepeatTerm() == 0
                )

                .map(currentDate -> {
                  Category category = routine.getCategory();
                  CategoryType type = category.getType();

                  Category parentCategory = null;
                  Category childCategory = null;

                  if (type == CategoryType.DEFAULT || type == CategoryType.MAJOR)  {
                    parentCategory = category; // 이미 연관된 엔티티
                    childCategory = null;
                  } else {
                    childCategory = category;
                    parentCategory = category.getParent(); // 연관관계로 접근 가능해야 함
                  }


                  RoutineSchedule routineSchedule = routineScheduleRepository
                      .findByRoutineAndScheduleDate(routine, currentDate);

                  return RoutineItemDTO.builder()
                      .scheduleId(routineSchedule != null ? routineSchedule.getId() : null)
                      .routineId(routine.getId())
                      .majorCategory(parentCategory != null ? parentCategory.getName()
                          : routine.getCategory().getName())
                      .subCategory(
                          childCategory != null ? childCategory.getName() : null)
                      .name(routine.getContent())
                      .triggerTime(routine.getTriggerTime())
                      .isDone(routineSchedule != null ? routineSchedule.getIsDone() : false)
                      .isImportant(routine.getIsImportant())
                      .date(currentDate)
                      .startDate(routine.getStartDate())
                      .repeatType(routine.getRepeatType())
                      .repeatValue(routine.getRepeatValue())
                      .build();
                })
        )
        .collect(Collectors.groupingBy(RoutineItemDTO::getDate));

    return groupedByDate;
  }




  /**
   * 사용자 루틴 단건 조회 입니다
   * return RoutineDetailDTO
   */
  public RoutineDetailDTO getDetailRoutine(Long routineId) {

    Routine routine = routineRepository.findByIdWithCategory(routineId)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_ROUTINE));

    Category category = routine.getCategory();
    CategoryType type = category.getType();

    Category parentCategory = null;
    Category childCategory = null;

    if (type == CategoryType.DEFAULT || type == CategoryType.MAJOR) {
      parentCategory = category; // 이미 연관된 엔티티
      childCategory = null;
    } else {
      childCategory = category;
      parentCategory = category.getParent();// 연관관계로 접근 가능해야 함
    }



    RoutineDetailDTO response = RoutineDetailDTO.builder()
        .routineId(routineId)
        .categoryId(routine.getCategory().getId())
        .majorCategory(parentCategory != null ? parentCategory.getName() : routine.getCategory().getName())
        .subCategory(childCategory != null ? childCategory.getName() : null)
        .name(routine.getContent())
        .triggerTime(routine.getTriggerTime())
        .isImportant(routine.getIsImportant())
        .repeatType(routine.getRepeatType())
        .repeatValue(routine.getRepeatValue())
        .startRoutineDate(routine.getStartDate())
        .build();

    return response;
  }

  /**
   * 사용자 당일 루틴 조회 입니다
   * return RoutinesDailyResponse
   * 지연로딩으로 routine 들고올때 category와 fetch join사용 했습니다
   * 스케줄러에 없을시 날짜 계산을 해서 루틴들고오는거를 만들었습니다
   */
  public List<RoutineTodayItemDTO> getTodayRoutines(String userEmail) {
    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_MEMBER));

    List<Routine> routines = routineRepository.findAllByMemberAndIsActiveWithCategory(member, true);

    List<RoutineTodayItemDTO> responseRoutines = routines.stream()

        .filter(routine ->
            !routine.getStartDate().isAfter(LocalDate.now()) &&
                routine.getRepeatType().isMatched(LocalDate.now(), routine.getRepeatValue()) &&
                ChronoUnit.WEEKS.between(routine.getStartDate(), LocalDate.now()) % routine.getRepeatTerm() == 0
        )

        .map(routine -> {
          Category category = routine.getCategory();
          CategoryType type = category.getType();

          Category parentCategory = null;
          Category childCategory = null;

          if (type == CategoryType.DEFAULT || type == CategoryType.MAJOR) {
            parentCategory = category; // 이미 연관된 엔티티
            childCategory = null;
          } else {
            childCategory = category;
            parentCategory = category.getParent();// 연관관계로 접근 가능해야 함
          }

          RoutineSchedule routineSchedule = routineScheduleRepository
              .findByRoutineAndScheduleDate(routine, LocalDate.now());

          return RoutineTodayItemDTO.builder()
              .scheduleId(routineSchedule != null ? routineSchedule.getId() : null)
              .routineId(routine.getId())
              .majorCategory(parentCategory != null ? parentCategory.getName() : routine.getCategory().getName())
              .subCategory(childCategory != null ? routine.getCategory().getName() : null)
              .name(routine.getContent())
              .triggerTime(routine.getTriggerTime())
              .isDone(routineSchedule != null ? routineSchedule.getIsDone() : false)
              .isImportant(routine.getIsImportant())
              .build();

        })
        .toList();


    return responseRoutines;
  }

  /**
   * 사용자  루틴 등록 입니다
   * return void
   */
  @Transactional
  public void createRoutine(RoutineSaveRequest routineSaveRequest, String userEmail) {

    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_MEMBER));

    Category category = categoryRepository.findById(routineSaveRequest.getCategoryId())
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_CATEGORY));



    /** 간단한 로직이라 DTO를 사용할 필요 없을거같아 바로 Routine으로 넣어줬습니다*/
    Routine routine = Routine.builder()
        .category(category)
        .content(routineSaveRequest.getName())
        .triggerTime(routineSaveRequest.getTriggerTime())
        .isImportant(routineSaveRequest.getIsImportant())
        .repeatType(routineSaveRequest.getRepeatType())
        .repeatValue(routineSaveRequest.getRepeatValue())
        .startDate(routineSaveRequest.getStartRoutineDate())
        .repeatTerm(routineSaveRequest.getRepeatInterval())
        .member(member)
        .build();

    routineRepository.save(routine);


  }

  /**
   * 사용자 루틴 수정 입니다
   * return void
   * transaction으로 updateRoutine에 넣어준다
   */
  @Transactional
  public void updateRoutine(Long routineId, RoutineUpdateRequest request) {

    Routine routine = routineRepository.findById(routineId)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_ROUTINE));


    if (request.getCategoryId() != null) {
      Category category = categoryRepository.findById(request.getCategoryId())
          .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_CATEGORY));
      routine.setCategory(category);
    }

    if (request.getName() != null) {
      routine.setContent(request.getName());
    }

    if (request.getTriggerTime() != null) {
      routine.setTriggerTime(request.getTriggerTime());
    }

    if (request.getIsImportant() != null) {
      routine.setIsImportant(request.getIsImportant());
    }

    if (request.getRepeatType() != null) {
      routine.setRepeatType(request.getRepeatType());
    }

    if (request.getRepeatValue() != null) {
      routine.setRepeatValue(request.getRepeatValue());
    }

    if (request.getStartRoutineDate() != null) {
      routine.setStartDate(request.getStartRoutineDate());
    }

    if (request.getRepeatInterval() != null) {
      routine.setRepeatTerm(request.getRepeatInterval());
    }
  }



  /**
   * 사용자 루틴 삭제 입니다
   * return void
   */
  @Transactional
  public void deleteRoutine(Long routineId) {

    Routine routine = routineRepository.findByIdWithMember(routineId)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_ROUTINE));

    routine.setIsActive(false);
    List<RoutineSchedule> routineSchedules = routineScheduleRepository.findByRoutine(routine);

    for (RoutineSchedule schedule : routineSchedules) {

      routineScheduleRepository.deleteById(schedule.getId());

    }

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
