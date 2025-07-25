package com.honlife.core.app.model.item.service;

import com.honlife.core.app.controller.admin.item.payload.AdminCreateItemRequest;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.dto.ItemDTO;
import com.honlife.core.app.model.item.repos.ItemRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminItemService {

    private ItemRepository itemRepository;
    private ItemService itemService;

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

    /**
     * 관리자 전용 아이템 생성 로직
     *
     * <p>요청으로 전달된 itemKey가 이미 존재하면 예외 발생</p>
     * <p>요청 값을 바탕으로 Item 엔티티를 생성하고, isListed 및 isActive 값을 true로 설정한 뒤 저장</p>
     *
     * @param request 아이템 생성 요청 DTO
     */
    @Transactional
    public void createItem(AdminCreateItemRequest request){
        if(itemService.itemKeyExists(request.getItemKey())){
            throw new CommonException(ResponseCode.GRANT_CONFLICT_ITEM);
        }

        Item item = Item.builder()
                .itemKey(request.getItemKey())
                .name(request.getItemName())
                .description(request.getItemDescription())
                .price(request.getItemPrice())
                .type(request.getItemType())
                .isListed(request.getIsListed())
                .build();
        itemRepository.save(item);
    }
}
