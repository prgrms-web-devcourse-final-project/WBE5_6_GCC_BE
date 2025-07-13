package com.honlife.core.app.controller.admin.routine.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "추천 루틴 프리셋 생성/수정 요청")
public class AdminRoutinePresetSaveRequest {

  @NotNull(message = "카테고리는 필수입니다")
  @Schema(description = "카테고리 ID", example = "1", required = true)
  private Long categoryId;

  @NotBlank(message = "루틴 내용은 필수입니다")
  @Size(max = 50, message = "루틴 내용은 50자를 초과할 수 없습니다")
  @Schema(description = "루틴 내용", example = "아침 스트레칭 하기", required = true)
  private String content;

  @Schema(description = "활성화 여부", example = "true")
  private Boolean isActive = true;
}
