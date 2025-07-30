package com.honlife.core.app.controller.routine.payload;

import com.honlife.core.app.model.routine.code.RepeatType;
import com.honlife.core.app.model.routine.dto.RoutineItemDTO;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
public class RoutinesResponse {

  private final Map<LocalDate, List<RoutineResponse>> routines;

  public RoutinesResponse(Map<LocalDate, List<RoutineResponse>> routines) {
    this.routines = routines;
  }

  public static RoutinesResponse fromDTO(Map<LocalDate, List<RoutineItemDTO>> dtoMap) {
    Map<LocalDate, List<RoutineResponse>> converted = dtoMap.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> entry.getValue().stream()
                .map(RoutineResponse::from)
                .collect(Collectors.toList())
        ));
    return new RoutinesResponse(converted);
  }

  @Getter
  @Builder
  @AllArgsConstructor
  public static class RoutineResponse {
    private Long scheduleId;
    private Long categoryId;
    private Long routineId;
    private String majorCategory;
    private String subCategory;
    private String name;
    private String triggerTime;
    private Boolean isDone;
    private Boolean isImportant;
    private LocalDate date;
    private LocalDate initDate;
    private RepeatType repeatType;
    private String repeatValue;

    public static RoutineResponse from(RoutineItemDTO dto) {
      return RoutineResponse.builder()
          .scheduleId(dto.getScheduleId())
          .categoryId(dto.getCategoryId())
          .routineId(dto.getRoutineId())
          .majorCategory(dto.getMajorCategory())
          .subCategory(dto.getSubCategory())
          .name(dto.getName())
          .triggerTime(dto.getTriggerTime())
          .isDone(dto.getIsDone())
          .isImportant(dto.getIsImportant())
          .date(dto.getDate())
          .initDate(dto.getInitDate())
          .repeatType(dto.getRepeatType())
          .repeatValue(dto.getRepeatValue())
          .build();
    }
  }
}
