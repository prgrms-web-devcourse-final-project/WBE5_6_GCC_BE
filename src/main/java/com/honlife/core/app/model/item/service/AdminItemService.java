package com.honlife.core.app.model.item.service;

import com.honlife.core.app.controller.admin.item.payload.AdminCreateItemRequest;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.repos.ItemRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminItemService {

    private ItemRepository itemRepository;

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
