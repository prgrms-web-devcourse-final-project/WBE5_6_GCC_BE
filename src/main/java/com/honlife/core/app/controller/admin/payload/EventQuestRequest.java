package com.honlife.core.app.controller.admin.payload;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "이벤트 퀘스트 등록/수정 요청 DTO")
public class EventQuestRequest {

    @Schema(description = "퀘스트 ID (수정 시 사용)", example = "1")
    private Long eventQuestId;

    @Size(max = 50)
    @Schema(description = "퀘스트 고유 키", example = "event_001")
    private String key;

    @Size(max = 255)
    @Schema(description = "퀘스트 이름", example = "이벤트 ! 3분 미션 ")
    private String name;

    @Schema(description = "퀘스트 설명", example = "잠들기 전 방 청소하기")
    private String info;

    @Schema(description = "이벤트 시작일 (ISO-8601 형식)", example = "2025-07-01T00:00:00+09:00")
    private OffsetDateTime startDate;

    @Schema(description = "이벤트 종료일 (ISO-8601 형식)", example = "2025-07-12T23:59:59+09:00")
    private OffsetDateTime endDate;
}