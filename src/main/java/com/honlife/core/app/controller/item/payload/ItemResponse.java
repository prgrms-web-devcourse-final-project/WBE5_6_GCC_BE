package com.honlife.core.app.controller.item.payload;

import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.dto.ItemDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ItemResponse {

    private Long itemId;
    private String itemKey;
    private String itemName;
    private String itemDescription;
    private ItemType itemType;
    private Integer itemPrice;
    private Boolean isOwned;
    private Boolean isListed;

    public static ItemResponse fromDTO(ItemDTO dto) {
        return ItemResponse.builder()
                .itemId(dto.getId())
                .itemKey(dto.getItemKey())
                .itemName(dto.getName())
                .itemDescription(dto.getDescription())
                .itemType(dto.getType())
                .itemPrice(dto.getPrice())
                .isOwned(dto.getIsOwned())
                .isListed(dto.getIsListed())
                .build();
    }

    public static List<ItemResponse> fromDTOList(List<ItemDTO> dtoList) {
        return dtoList.stream()
                .map(ItemResponse::fromDTO)
                .toList();
    }
}
