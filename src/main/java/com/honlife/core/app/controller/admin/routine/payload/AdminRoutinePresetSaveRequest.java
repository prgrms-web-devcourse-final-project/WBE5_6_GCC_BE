package com.honlife.core.app.controller.admin.routine.payload;

import com.honlife.core.app.model.routine.code.RepeatType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRoutinePresetSaveRequest {

  @NotNull(message = "카테고리는 필수입니다")
  private Long categoryId;

  private String triggerTime;

  private Boolean isImportant;

  @NotNull(message = "반복 유형은 필수입니다")
  private RepeatType repeatType = RepeatType.DAILY;

  private String repeatValue;


  @NotBlank(message = "루틴 내용은 필수입니다")
  @Size(max = 50, message = "루틴 내용은 50자를 초과할 수 없습니다")
  private String name;

  private int repeatTerm = 1;
}
