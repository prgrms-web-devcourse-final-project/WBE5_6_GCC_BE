package com.honlife.core.app.model.member.model;

import com.honlife.core.app.model.item.code.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberItemDTOCustom {
    private String itemKey;
    private String itemName;
    private ItemType itemtype;
    private String itemDescription;
    private Boolean isEquipped;
}
