package com.honlife.core.app.controller.member.payload;

import com.honlife.core.app.model.item.code.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberItemResponse {

    private String itemKey;
    private String itemName;
    private ItemType itemtype;
    private String itemDescription;
    private Boolean isEquipped;


}
