package com.honlife.core.app.controller.admin.item.payload;

import com.honlife.core.app.model.item.code.ItemType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminItemResponse {

    private Long itemId;
    private String itemKey;
    private String itemName;
    private Integer itemPrice;
    private ItemType itemType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean isActive;
}
