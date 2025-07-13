package com.honlife.core.app.controller.admin.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "퀘스트 목록 항목")
public class QuestResponse {
  @Schema(description = "퀘스트 ID", example = "1")
  @NotBlank
  private Long questId;

  @Schema(description = "퀘스트 이름", example = "청소 루틴 3번 완료하기")
  @NotBlank
  private String name;


  @Schema(description = "보상 포인트", example = "100")
  private int reward;

  @Schema(description = "카테고리 ID", example = "1")
  private int categoryName;

  @Schema(description = "시작일", example = "2025-07-01T00:00:00+09:00")
  private OffsetDateTime startDate;

  @Schema(description = "종료일", example = "2025-07-12T23:59:59+09:00")
  private OffsetDateTime endDate;

}
