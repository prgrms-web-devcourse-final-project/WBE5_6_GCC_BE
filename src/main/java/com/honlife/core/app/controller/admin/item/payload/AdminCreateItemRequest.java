package com.honlife.core.app.controller.admin.item.payload;

import com.honlife.core.app.model.item.code.ItemType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AdminCreateItemRequest {
    @NotEmpty
    private String itemKey;
    @NotEmpty
    private ItemType itemType;
    @NotEmpty
    private String itemName;
    @NotEmpty
    private Integer itemPrice;

    private String itemDescription;
}
