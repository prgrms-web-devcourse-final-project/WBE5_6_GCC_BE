package com.honlife.core.app.model.routine.dto;

import com.honlife.core.app.model.routine.code.RepeatType;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RoutineTodayItemDTO {
  private Long scheduleId;


  private Long routineId;


  private String majorCategory;


  private String subCategory;


  private String name;


  private String triggerTime;


  private Boolean isDone;


  private Boolean isImportant;

  private RepeatType repeatType;

  private String repeatValue;

}
