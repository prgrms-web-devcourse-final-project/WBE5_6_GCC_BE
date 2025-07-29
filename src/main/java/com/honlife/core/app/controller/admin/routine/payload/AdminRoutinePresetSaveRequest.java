package com.honlife.core.app.controller.admin.routine.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import com.honlife.core.app.model.routine.code.RepeatType;

@Getter
@Setter
@Schema(description = "추천 루틴 프리셋 생성/수정 요청")
public class AdminRoutinePresetSaveRequest {

  @NotNull(message = "카테고리는 필수입니다")
  @Schema(description = "카테고리 ID", example = "1", required = true)
  private Long categoryId;


  @Schema(description = "트리거 시간", example = "09:00")
  private String triggerTime;

  @Schema(description = "중요 루틴 여부", example = "false")
  private Boolean isImportant;

  @NotNull(message = "반복 유형은 필수입니다")
  @Schema(description = "반복 유형", example = "WEEKLY")
  private RepeatType repeatType;

  @Schema(description = "반복 값", example = "1,3,5")
  private String repeatValue;

  @NotNull(message = "루틴 시작 날짜는 필수입니다")
  @Schema(description = "루틴 시작 날짜", example = "2025-07-01")
  private LocalDate initDate;

  @NotBlank(message = "루틴 내용은 필수입니다")
  @Size(max = 50, message = "루틴 내용은 50자를 초과할 수 없습니다")
  @Schema(description = "루틴 내용", example = "아침 스트레칭 하기", required = true)
  private String name;

  @Schema(description = "주 반복 간격 (1 = 매주, 2 = 격주 등)", example = "1")
  private Integer repeatTerm = 1;  // 기본값: 매주




}
