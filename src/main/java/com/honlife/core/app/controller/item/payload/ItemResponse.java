package com.honlife.core.app.controller.item.payload;

import com.honlife.core.app.model.item.code.ItemType;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private Long itemId;
    private String itemKey;
    private String itemName;
    private String itemDescription;
    private ItemType itemType;
    private Integer itemPoint;
    private Boolean isOwned;
}
