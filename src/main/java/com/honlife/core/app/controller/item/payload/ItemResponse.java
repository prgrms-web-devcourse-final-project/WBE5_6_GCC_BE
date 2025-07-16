package com.honlife.core.app.controller.item.payload;

import com.honlife.core.app.model.item.code.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponse {
    private Long itemId;

    private ItemType itemType;

    private String itemKey;

    private String itemName;

    private String itemDescription;

    private Integer itemPoint;

    private Boolean isOwned;
}
