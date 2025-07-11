package com.honlife.core.app.controller.item.payload;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemResponse {
    private Long itemId;

    private String itemType;

    private String itemKey;

    private String itemName;

    private Integer itemPoint;
}
