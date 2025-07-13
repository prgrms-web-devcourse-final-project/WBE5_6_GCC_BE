package com.honlife.core.app.controller.admin.badge.payload;

import com.honlife.core.app.model.badge.code.BadgeTier;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdminBadgeSaveRequest {
    
    @NotBlank
    @Schema(description = "업적의 키", example = "clean_bronze")
    private String badgeKey;

    @NotBlank
    @Schema(description = "업적의 이름", example = "청소 초보")
    private String badgeName;

    @Schema(description = "업적의 티어", example = "BRONZE")
    private BadgeTier tier;

    @NotBlank
    @Schema(description = "달성 조건(문장)", example = "청소 루틴 5번 하기")
    private String how;

    @NotNull
    @Schema(description = "달성 조건(숫자)", example = "5")
    private Integer requirement;

    @Schema(description = "업적 설명", example = "이제 청소 좀 한다고 말할 수 있겠네요!")
    private String info;

    @Schema(description = "업적과 연관된 카테고리", example = "1")
    private Long categoryId;

}
