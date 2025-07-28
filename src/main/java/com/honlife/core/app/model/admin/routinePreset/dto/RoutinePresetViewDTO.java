package com.honlife.core.app.model.admin.routinePreset.dto;


import com.honlife.core.app.model.routine.code.RepeatType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutinePresetViewDTO {

  private Long presetId;

  private Long categoryId;

  private String majorCategory;


  private String name;

  private String triggerTime;

  private Boolean isImportant;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private String emoji;

  private RepeatType repeatType;

  private String repeatValue;

  private LocalDate initDate;

  private int repeatTerm;


}
