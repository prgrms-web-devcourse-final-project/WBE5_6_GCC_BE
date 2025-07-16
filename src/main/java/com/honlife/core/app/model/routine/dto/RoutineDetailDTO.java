package com.honlife.core.app.model.routine.dto;
import com.honlife.core.app.model.routine.code.RepeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineDetailDTO {
  private Long routineId;
  private Long categoryId;
  private String majorCategory;
  private String subCategory;
  private String name;
  private String triggerTime;
  private Boolean isImportant;
  private RepeatType repeatType;
  private String repeatValue;

}
