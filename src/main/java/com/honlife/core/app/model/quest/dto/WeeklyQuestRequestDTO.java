package com.honlife.core.app.model.quest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "주간 퀘스트 등록 요청 DTO")
public class WeeklyQuestRequestDTO {

    @Schema(description = "퀘스트 ID (수정 시 사용)", example = "1")
    private Long id;

    @Schema(description = "생성 시간", example = "2025-07-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시간", example = "2025-07-05T12:00:00")
    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    @Schema(description = "활성 여부", example = "true")
    private Boolean isActive;

    @Size(max = 50)
    @Schema(description = "퀘스트 고유 키", example = "clean_weekly_3")
    private String key;

    @Size(max = 255)
    @Schema(description = "퀘스트 이름", example = "청소 루틴 3번 완료하기")
    private String name;

    @Schema(description = "퀘스트 설명", example = "정해진 청소 루틴을 일주일에 3회 완료하세요.")
    private String info;
}
