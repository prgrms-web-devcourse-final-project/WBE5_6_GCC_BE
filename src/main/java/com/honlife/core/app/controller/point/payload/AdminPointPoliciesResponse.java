package com.honlife.core.app.controller.point.payload;

import com.honlife.core.app.model.point.code.PointSourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "포인트 정책 목록 조회 응답")
public class AdminPointPoliciesResponse {

    @Schema(description = "정책 목록")
    private List<PolicyItem> policies;

    @Getter
    @Setter
    @Builder
    @Schema(description = "정책 아이템")
    public static class PolicyItem {

        @Schema(description = "정책 ID", example = "1")
        private Long id;

        @Schema(description = "포인트 소스 타입", example = "ROUTINE")
        private PointSourceType type;

        @Schema(description = "참조 키", example = "ROUTINE_COMPLETE")
        private String referenceKey;

        @Schema(description = "지급 포인트", example = "10")
        private Integer point;

        @Schema(description = "활성화 여부", example = "true")
        private Boolean isActive;

        @Schema(description = "생성일시", example = "2025-01-15T10:30:00")
        private LocalDateTime createdAt;

        @Schema(description = "수정일시", example = "2025-01-15T14:20:00")
        private LocalDateTime updatedAt;
    }
}
