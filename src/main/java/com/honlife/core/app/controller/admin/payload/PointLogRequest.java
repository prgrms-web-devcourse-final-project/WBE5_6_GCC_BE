package com.honlife.core.app.controller.admin.payload;

import com.honlife.core.app.model.point.code.PointLogType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PointLogRequest {

    @Schema(description = "사용자 아이디", example = "1L")
    private Integer memberId;

    @Schema(description = "포인트 로그 타입 (GET: 지급, USE: 사용)", example = "GET")
    private PointLogType pointLogType;

    @Schema(description = "조회 시작일 (yyyy-MM-dd'T'HH:mm:ss)", example = "2025-07-01T00:00:00")
    private LocalDateTime startDateTime;

    @Schema(description = "조회 종료일 (yyyy-MM-dd'T'HH:mm:ss)", example = "2025-07-14T00:00:00")
    private LocalDateTime endDateTime;
}
