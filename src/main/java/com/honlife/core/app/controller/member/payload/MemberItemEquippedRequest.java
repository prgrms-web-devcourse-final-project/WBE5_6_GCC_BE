package com.honlife.core.app.controller.member.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberItemEquippedRequest {
    @Schema(description = "착용하고 있던 아이템", example = "top_item_01")
    private String oldItemKey;

    @Schema(description = "새로 착용 할 아이템", example = "top_item_02")
    private String newItemKey;
}
