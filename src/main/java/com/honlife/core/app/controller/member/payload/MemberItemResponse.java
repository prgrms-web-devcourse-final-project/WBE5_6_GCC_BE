package com.honlife.core.app.controller.member.payload;

import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.member.model.MemberItemDTOCustom;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MemberItemResponse {

    private String itemKey;
    private String itemName;
    private ItemType itemtype;
    private String itemDescription;
    private Boolean isEquipped;

    public static MemberItemResponse fromDTO(MemberItemDTOCustom dto) {
        return MemberItemResponse.builder()
                .itemKey(dto.getItemKey())
                .itemName(dto.getItemName())
                .itemtype(dto.getItemtype())
                .itemDescription(dto.getItemDescription())
                .isEquipped(dto.getIsEquipped())
                .build();
    }
    public static List<MemberItemResponse> fromDTOList(List<MemberItemDTOCustom> dtos) {
        return dtos.stream()
                .map(MemberItemResponse::fromDTO)
                .toList();
    }


}
