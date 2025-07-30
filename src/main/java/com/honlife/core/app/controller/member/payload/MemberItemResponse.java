package com.honlife.core.app.controller.member.payload;

import com.honlife.core.app.model.item.code.ItemType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberItemResponse {


    private Long id;
    private String itemKey;
    private String itemName;
    private ItemType itemtype;
    private String itemDescription;
    private Boolean isEquipped;

}
