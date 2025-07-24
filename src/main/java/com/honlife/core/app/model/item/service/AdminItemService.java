package com.honlife.core.app.model.item.service;

import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.dto.ItemDTO;
import com.honlife.core.app.model.item.repos.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminItemService {

    private final ItemRepository itemRepository;

    /**
     * 전체 아이템 정보를 조회합니다.
     * 이 메서드는 관리자 상점 등에서 전체 아이템 목록을 불러올 때 사용됩니다.
     * isActive 또는 isListed 여부와 상관없이 모든 아이템을 조회합니다.
     *
     * @return List<ItemDTO> - 아이템 정보 DTO 리스트
     */
    public List<ItemDTO> getAllItems() {
        List<Item> items = itemRepository.findAllByIsActiveTrue(); // QueryDSL 결과

        return items.stream()
                .map(item -> ItemDTO.builder()
                        .id(item.getId())
                        .itemKey(item.getItemKey())
                        .name(item.getName())
                        .description(item.getDescription())
                        .price(item.getPrice())
                        .type(item.getType())
                        .isListed(item.getIsListed())
                        .isActive(item.getIsActive())
                        .build())
                .toList();
    }
}
