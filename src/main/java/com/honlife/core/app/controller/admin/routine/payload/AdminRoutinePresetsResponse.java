package com.honlife.core.app.controller.admin.routine.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "추천 루틴 프리셋 목록 조회 응답")
public class AdminRoutinePresetsResponse {

  @Schema(description = "프리셋 목록")
  private List<PresetItem> presets;

  @Getter
  @Setter
  @Builder
  @Schema(description = "프리셋 아이템")
  public static class PresetItem {

    @Schema(description = "프리셋 ID", example = "1")
    private Long presetId;

    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;

    @Schema(description = "카테고리 이름", example = "청소")
    private String categoryName;

    @Schema(description = "루틴 내용", example = "아침 스트레칭 하기")
    private String content;

    @Schema(description = "활성화 여부", example = "true")
    private Boolean isActive;

    @Schema(description = "생성일시", example = "2025-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-01-15T14:20:00")
    private LocalDateTime updatedAt;
  }
}