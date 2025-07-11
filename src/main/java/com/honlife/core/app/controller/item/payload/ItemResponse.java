package com.honlife.core.app.controller.item.payload;


import com.honlife.core.app.model.item.code.ItemType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemResponse {
    private Long itemId;

    @Schema(description = "아이템 타입", example = "TOP",
            allowableValues = {"TOP", "BOTTOM", "ACCESSORY"})
    private ItemType itemType;


    private String itemKey;

    private String itemName;

    private Integer itemPoint;
}
