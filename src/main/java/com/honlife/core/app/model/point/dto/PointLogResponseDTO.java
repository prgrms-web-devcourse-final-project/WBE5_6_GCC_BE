package com.honlife.core.app.model.point.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "포인트 지급/소비 기록 응답 DTO")
public class PointLogResponseDTO {

  @Schema(description = "기록 ID", example = "10")
  private Long id;

  @Schema(description = "이메일", example = "aakjdf@naver.com")
  private String email;

  @Schema(description = "타입 (GET/USE)", example = "GET")
  private String type;

  @Schema(description = "포인트 양", example = "1000")
  private Integer point;

  @Schema(description = "사유", example = "퀘스트 완료")
  private String reason;

  @Schema(description = "발생 시간", example = "2025-07-05T14:00:00")
  private LocalDateTime time;
}

