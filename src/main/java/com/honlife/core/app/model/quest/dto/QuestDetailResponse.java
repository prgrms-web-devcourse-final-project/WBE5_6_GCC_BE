package com.honlife.core.app.model.quest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "퀘스트 상세 DTO")
public class QuestDetailResponse {

  @Schema(description = "퀘스트 ID", example = "1")
  private Long id;

  @Schema(description = "퀘스트 키", example = "weekly_clean_3times")
  private String key;

  @Schema(description = "퀘스트 타입", example = "WEEKLY")
  private String type;

  @Schema(description = "퀘스트 이름", example = "청소 루틴 3번 완료하기")
  private String name;

  @Schema(description = "퀘스트 설명", example = "정해진 청소 루틴을 일주일에 3회 완료하세요.")
  private String info;

  @Schema(description = "보상 포인트", example = "100")
  private int reward;

  @Schema(description = "카테고리 ID", example = "1")
  private int categoryId;

  @Schema(description = "종료일", example = "2025-07-15")
  private String endDate;
}

