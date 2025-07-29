package com.honlife.core.app.controller.admin.routine.payload;

import com.honlife.core.app.model.routine.code.RepeatType;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminRoutinePresetUpdateRequest {

  private Long categoryId;

  private String triggerTime;

  private Boolean isImportant;

  private RepeatType repeatType;

  private String repeatValue;

  private LocalDate initDate;

  @Size(max = 50, message = "루틴 내용은 50자를 초과할 수 없습니다")
  private String name;

  private Integer repeatTerm ;

}
