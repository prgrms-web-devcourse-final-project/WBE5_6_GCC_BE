package com.honlife.core.app.model.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "RoutinePresetRequestDTO", description = "추천 루틴 추가 요청 DTO")
public class RoutinePresetRequestDTO {

  @NotNull
  @Schema(description = "카테고리 ID", example = "1")
  private Integer categoryId;

  @NotNull
  @Size(max = 255)
  @Schema(description = "추천 루틴 내용", example = "이불 개기, 책상 닦기")
  private String content;
}
