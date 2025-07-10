package com.honlife.core.app.controller.item.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemPayload {
    private Long itemId;

    private String type;

    private String itemKey;

    private String itemName;

    private Integer itemPoint;
}
