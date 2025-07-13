package com.honlife.core.app.controller.admin.point.payload;

import com.honlife.core.app.model.point.code.PointSourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "포인트 정책 생성/수정 요청")
public class AdminPointPolicyRequest {

    @NotNull(message = "포인트 소스 타입은 필수입니다")
    @Schema(description = "포인트 소스 타입", example = "ROUTINE", required = true)
    private PointSourceType type;

    @NotBlank(message = "참조 키는 필수입니다")
    @Size(max = 50, message = "참조 키는 50자를 초과할 수 없습니다")
    @Schema(description = "참조 키 (고유한 식별자)", example = "ROUTINE_COMPLETE", required = true)
    private String referenceKey;

    @NotNull(message = "포인트는 필수입니다")
    @Min(value = 1, message = "포인트는 1 이상이어야 합니다")
    @Schema(description = "지급할 포인트", example = "10", required = true)
    private Integer point;

    @Schema(description = "활성화 여부", example = "true")
    private Boolean isActive = true;
}