package com.honlife.core.app.controller.admin.routine.payload;

import com.honlife.core.app.model.routine.code.RepeatType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminRoutinePresetUpdateRequest {

  @NotNull
  private Long categoryId;

  @NotNull
  private String triggerTime;

  @NotNull
  private Boolean isImportant;

  @NotNull
  private RepeatType repeatType;

  @NotNull
  private String repeatValue;

  @NotNull
  private LocalDate initDate;

  @Size(max = 50, message = "루틴 내용은 50자를 초과할 수 없습니다")
  private String name;

  @NotNull
  private Integer repeatTerm ;

}
