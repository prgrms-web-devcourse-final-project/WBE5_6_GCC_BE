package com.honlife.core.app.controller.admin.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "QuestUpdateDTO", description = "퀘스트 수정 요청 DTO")
public class QuestUpdateRequest {


  @Size(max = 255)
  @Schema(description = "퀘스트 이름", example = "이벤트 푸만! (수정)")
  private String name;

  @Schema(description = "퀘스트 설명", example = "수정된 퀘스트 설명입니다.")
  private String info;

  //nullable 허용
  @Schema(description = "이벤트 시작일", example = "2025-07-01T00:00:00+09:00")
  private OffsetDateTime startDate;

  //nullable 허용
  @Schema(description = "이벤트 종료일", example = "2025-07-12T23:59:59+09:00")
  private OffsetDateTime endDate;
}