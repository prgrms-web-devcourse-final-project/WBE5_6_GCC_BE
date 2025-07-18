package com.honlife.core.app.controller.admin.quest.payload;

import com.honlife.core.app.model.point.code.PointSourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "퀘스트 상세 DTO")
public class QuestDetailResponse {

  @Schema(description = "퀘스트 ID", example = "1")
  private Long questId;

  @Schema(description = "퀘스트 키", example = "weekly_clean_3times")
  private String key;

  @Schema(description = "퀘스트 타입", example = "WEEKLY")
  private PointSourceType type;

  @Schema(description = "퀘스트 이름", example = "청소 루틴 3번 완료하기")
  private String name;

  @Schema(description = "퀘스트 설명", example = "정해진 청소 루틴을 일주일에 3회 완료하세요.")
  private String info;

  @Schema(description = "보상 포인트", example = "100")
  private int reward;


  @Schema(description = "이벤트 시작일 (ISO-8601 형식)", example = "2025-07-01T00:00:00+09:00")
  //nullable 허용
  private LocalDateTime startDate;

  @Schema(description = "이벤트 종료일 (ISO-8601 형식)", example = "2025-07-12T23:59:59+09:00")
  //nullable 허용
  private LocalDateTime endDate;
}

