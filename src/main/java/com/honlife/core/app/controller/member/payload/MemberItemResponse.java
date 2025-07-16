package com.honlife.core.app.controller.member.payload;

import com.honlife.core.app.model.item.code.ItemType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberItemResponse {

    private String itemKey;
    private String itemName;
    private ItemType itemtype;
    private Boolean isEquipped;


}
