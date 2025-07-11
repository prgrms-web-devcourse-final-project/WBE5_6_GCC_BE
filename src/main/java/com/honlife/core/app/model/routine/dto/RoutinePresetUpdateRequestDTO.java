package com.honlife.core.app.model.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "RoutinePresetUpdateDTO", description = "추천 루틴 수정 요청 DTO")
public class RoutinePresetUpdateRequestDTO {

  @NotNull
  @Schema(description = "루틴 프리셋 ID", example = "1")
  private Long routinePresetId;

  @Size(max = 50)
  @Schema(description = "수정할 루틴 내용", example = "침대 정리, 창문 열기")
  private String content;

  @NotNull
  @Schema(description = "카테고리 ID", example = "2")
  private Long categoryId;
}