package com.honlife.core.app.model.item.service;

import java.util.List;
import java.util.stream.Collectors;

import com.honlife.core.app.controller.item.payload.ItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.dto.ItemDTO;
import com.honlife.core.app.model.item.repos.ItemRepository;
import com.honlife.core.app.model.member.domain.MemberItem;
import com.honlife.core.app.model.member.repos.MemberItemRepository;
import com.honlife.core.infra.util.NotFoundException;
import com.honlife.core.infra.util.ReferencedWarning;


@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<ItemResponse> getAllItems(ItemType itemType) {
        List<Item> items;

        if (itemType != null) {
            items = itemRepository.findByTypeAndIsActiveTrue(itemType);
        } else {
            items = itemRepository.findAllByIsActiveTrue();
        }

        return items.stream()
                .map(item -> ItemResponse.builder()
                        .itemId(item.getId())
                        .itemKey(item.getItemKey())
                        .itemName(item.getName())
                        .itemType(item.getType())
                        .itemPoint(item.getPrice())
                        .build())
                .collect(Collectors.toList());
    }
    /**
     * itemKey로 단일 아이템 조회
     */
    public ItemResponse getItemByKey(String itemKey) {
        Item item = itemRepository.findByItemKeyAndIsActiveTrue(itemKey)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다."));

        return ItemResponse.builder()
                .itemId(item.getId())
                .itemKey(item.getItemKey())
                .itemName(item.getName())
                .itemType(item.getType())
                .itemPoint(item.getPrice())
                .build();
    }

    public boolean itemKeyExists(final String itemKey) {
        return itemRepository.existsByItemKeyIgnoreCase(itemKey);
    }
    private ItemDTO mapToDTO(final Item item, final ItemDTO itemDTO) {
        itemDTO.setCreatedAt(item.getCreatedAt());
        itemDTO.setUpdatedAt(item.getUpdatedAt());
        itemDTO.setIsActive(item.getIsActive());
        itemDTO.setId(item.getId());
        itemDTO.setItemKey(item.getItemKey());
        itemDTO.setName(item.getName());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setType(item.getType());
        return itemDTO;
    }

    private Item mapToEntity(final ItemDTO itemDTO, final Item item) {
        item.setCreatedAt(itemDTO.getCreatedAt());
        item.setUpdatedAt(itemDTO.getUpdatedAt());
        item.setIsActive(itemDTO.getIsActive());
        item.setItemKey(itemDTO.getItemKey());
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        item.setType(itemDTO.getType());
        return item;
    }

    public ItemDTO get(final Long id) {
        return itemRepository.findById(id)
                .map(item -> mapToDTO(item, new ItemDTO()))
                .orElseThrow(NotFoundException::new);
    }
}
