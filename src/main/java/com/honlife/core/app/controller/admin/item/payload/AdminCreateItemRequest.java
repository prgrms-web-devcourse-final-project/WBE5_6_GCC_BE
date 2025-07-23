package com.honlife.core.app.controller.admin.item.payload;

import com.honlife.core.app.model.item.code.ItemType;
import lombok.Data;

@Data
public class AdminCreateItemRequest {
    private String itemKey;
    private ItemType itemType;
    private String itemName;
    private String itemDescription;
    private Integer itemPrice;
}
