package com.honlife.core.app.controller.admin.item.payload;

import com.honlife.core.app.model.item.code.ItemType;
import java.time.LocalDateTime;
import java.util.List;

import com.honlife.core.app.model.item.dto.ItemDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminItemResponse {

    private Long itemId;
    private String itemKey;
    private String itemName;
    private String itemDescription;
    private Integer itemPrice;
    private ItemType itemType;
    private Boolean isOwned;
    private Boolean isListed;
    private Boolean isActive;

    public static AdminItemResponse fromDTO(ItemDTO dto) {
        return AdminItemResponse.builder()
                .itemId(dto.getId())
                .itemKey(dto.getItemKey())
                .itemName(dto.getName())
                .itemDescription(dto.getDescription())
                .itemPrice(dto.getPrice())
                .itemType(dto.getType())
                .isOwned(dto.getIsOwned())
                .isListed(dto.getIsListed())
                .isActive(dto.getIsActive())
                .build();
    }
    public static List<AdminItemResponse> fromDTOList(List<ItemDTO> dtos) {
        return dtos.stream()
                .map(AdminItemResponse::fromDTO)
                .toList();
    }
}
