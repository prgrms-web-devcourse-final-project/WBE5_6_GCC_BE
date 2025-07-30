package com.honlife.core.app.model.routine.service;

import com.honlife.core.app.model.category.dto.CategoryDTO;
import com.honlife.core.app.model.category.service.CategoryService;
import com.honlife.core.app.model.routine.code.RepeatType;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.app.controller.routine.payload.RoutineUpdateRequest;
import com.honlife.core.app.model.category.code.CategoryType;
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
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.honlife.core.app.model.routine.repos.RoutineRepository;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
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
                    !routine.getInitDate().isAfter(currentDate)  &&
                        routine.getRepeatType().isMatched(currentDate, routine.getRepeatValue()) &&
                        // 달마다 차이나는 것을 무시하고 순수한 주차 사이 값을 돌려준다는 메서드인데 진짜 너무 대박이네요
                        isToday(routine, currentDate)
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
                      .findByRoutineAndScheduledDate(routine, currentDate);

                  return RoutineItemDTO.builder()
                      .scheduleId(routineSchedule != null ? routineSchedule.getId() : null)
                      .routineId(routine.getId())
                      .categoryId(routine.getCategory().getId())
                      .majorCategory(parentCategory != null ? parentCategory.getName()
                          : routine.getCategory().getName())
                      .subCategory(
                          childCategory != null ? childCategory.getName() : null)
                      .name(routine.getContent())
                      .triggerTime(routine.getTriggerTime())
                      .isDone(routineSchedule != null ? routineSchedule.getIsDone() : false)
                      .isImportant(routine.getIsImportant())
                      .date(currentDate)
                      .initDate(routine.getInitDate())
                      .repeatType(routine.getRepeatType())
                      .repeatValue(routine.getRepeatValue())
                      .emoji(routine.getCategory().getEmoji())
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
        .initDate(routine.getInitDate())
        .emoji(routine.getCategory().getEmoji())
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
            !routine.getInitDate().isAfter(LocalDate.now()) &&
                routine.getRepeatType().isMatched(LocalDate.now(), routine.getRepeatValue()) &&
                isToday(routine, LocalDate.now())
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
              .findByRoutineAndScheduledDate(routine, LocalDate.now());

          return RoutineTodayItemDTO.builder()
              .scheduleId(routineSchedule != null ? routineSchedule.getId() : null)
              .routineId(routine.getId())
              .categoryId(routine.getCategory().getId())
              .majorCategory(parentCategory != null ? parentCategory.getName() : routine.getCategory().getName())
              .subCategory(childCategory != null ? routine.getCategory().getName() : null)
              .name(routine.getContent())
              .triggerTime(routine.getTriggerTime())
              .isDone(routineSchedule != null ? routineSchedule.getIsDone() : false)
              .isImportant(routine.getIsImportant())
              .initDate(routine.getInitDate())
              .repeatType(routine.getRepeatType())
              .repeatValue(routine.getRepeatValue())
              .emoji(routine.getCategory().getEmoji())
              .build();

        })
        .toList();


    return responseRoutines;
  }

  /**
   * 사용자  루틴 등록 입니다
   * 프론트에서 받은 카테고리 Id로 저장합니다
   * return void
   */
  @Transactional
  public void createRoutine(RoutineSaveRequest routineSaveRequest, String userEmail) {

    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_MEMBER));

    Category category = categoryRepository.findById(routineSaveRequest.getCategoryId())
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_CATEGORY));

    /** 간단한 로직이라 DTO를 사용할 필요 없을거같아 바로 Routine으로 넣어줬습니다*/
    Routine routine = new Routine();
    int repeatTerm;
    if(routineSaveRequest.getRepeatTerm() == null || routineSaveRequest.getRepeatTerm() == 0){
        repeatTerm = 1;
    }
    else{
      repeatTerm = routineSaveRequest.getRepeatTerm();
    }

    routine.setCategory(category);
    routine.setContent(routineSaveRequest.getName());
    routine.setTriggerTime(routineSaveRequest.getTriggerTime());
    routine.setIsImportant(routineSaveRequest.getIsImportant());
    routine.setRepeatType(routineSaveRequest.getRepeatType());
    routine.setRepeatValue(routineSaveRequest.getRepeatValue());
    routine.setInitDate(routineSaveRequest.getInitDate());
    routine.setRepeatTerm(repeatTerm);
    routine.setMember(member);

    routineRepository.save(routine);

    // 오늘이 루틴에 해당하는 날짜인지 확인
      createTodayRoutine(routine);
  }

    /**
     * 생성 수정 시에 오늘 루틴이 추가되어야 할 경우 추가합니다.
     * @param routine 루틴 데이터
     */
    private void createTodayRoutine(Routine routine) {
        if(!routine.getInitDate().isAfter(LocalDate.now()) && routine.getRepeatType().isMatched(LocalDate.now(), routine.getRepeatValue()) &&
            isToday(routine, LocalDate.now())){
            boolean exists = routineScheduleRepository.existsByRoutineAndScheduledDate(routine, LocalDate.now());
              if (!exists) {
              RoutineSchedule schedule = RoutineSchedule.builder()
                  .scheduledDate(LocalDate.now())
                  .isDone(false)
                  .routine(routine)
                  .build();
              routineScheduleRepository.save(schedule);
              }
          }
    }

    /**
     * term 을 확인해서 오늘이 루틴을 행하는 날인지 확인합니다.
     * @param routine 루틴 정보
     * @return Boolean
     */
  // todo 이름 바꾸기
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
   * 사용자 루틴 수정 입니다
   * return void
   * 수정도 카테고리 Id롤 받습니다
   * transaction으로 updateRoutine에 넣어준다
   */
  @Transactional
  public void updateRoutine(Long routineId, RoutineUpdateRequest request) {

    Routine routine = routineRepository.findById(routineId)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_ROUTINE));


      Category category = categoryRepository.findById(request.getCategoryId())
          .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_CATEGORY));

      routine.setCategory(category);
      routine.setContent(request.getName());
      routine.setTriggerTime(request.getTriggerTime());
      routine.setIsImportant(request.getIsImportant());
      routine.setRepeatType(request.getRepeatType());
      routine.setRepeatValue(request.getRepeatValue());
      routine.setInitDate(request.getInitDate());
      routine.setRepeatTerm(request.getRepeatTerm());
      routineRepository.save(routine);

      createTodayRoutine(routine);
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

      schedule.setIsActive(false);

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

    /**
     * 해당 카테고리를 참조 중인 모든 루틴에서 카테고리 참조를 제거합니다. 루틴의 category 필드를 null로 설정합니다.
     * @param categoryId 카테고리 아이디
     */
    @Transactional
    public void removeCategoryReference(Long categoryId, String userEmail) {

        Category category = categoryRepository.findCategoryById(categoryId, userEmail).orElseThrow(()->new CommonException(ResponseCode.NOT_FOUND_CATEGORY));

        if(category.getType() == CategoryType.MAJOR){
            category.getChildren().forEach(child -> {
                List<Routine> routines = routineRepository.findAllByCategory_Id(child.getId());

                routines.forEach(routine -> {
                    routine.setCategory(null);
                });
            });
        }

        List<Routine> routines = routineRepository.findAllByCategory_Id(categoryId);

        routines.forEach(routine -> {
            routine.setCategory(null);
        });

    }
}
