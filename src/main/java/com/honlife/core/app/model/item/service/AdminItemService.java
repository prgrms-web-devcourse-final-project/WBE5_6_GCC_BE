package com.honlife.core.app.model.item.service;

import com.honlife.core.app.controller.admin.item.payload.AdminCreateItemRequest;
import com.honlife.core.app.controller.admin.item.payload.AdminItemRequest;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.repos.ItemRepository;
import com.honlife.core.app.model.member.domain.MemberItem;
import com.honlife.core.app.model.member.service.MemberItemService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminItemService {

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final MemberItemService memberItemService;

    /**
     * 관리자 - 아이템 수정 서비스
     *
     * @param itemKey 수정 대상 아이템의 고유 키
     * @param request 수정할 값이 담긴 요청 DTO (이름, 설명, 가격, 타입)
     *
     * <p><b>[설명]</b></p>
     * - itemKey로 아이템을 조회한 후, 전달받은 값으로 필드를 갱신합니다.
     * - 수정 대상 필드: name, description, price, type
     * - 존재하지 않는 아이템일 경우 NOT_FOUND 예외 발생
     */
    @Transactional
    public void updateItem(Long itemId, AdminItemRequest request) {
        Item item = itemService.getItemById(itemId)
                .orElseThrow(()->new CommonException(ResponseCode.NOT_FOUND_ITEM));
        item.setName(request.getItemName());
        item.setDescription(request.getItemDescription());
        item.setPrice(request.getPrice());
        item.setType(request.getItemType());

        if (request.getIsListed() != null) {
            item.setIsListed(request.getIsListed());
        }
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
                .key(request.getItemKey())
                .name(request.getItemName())
                .description(request.getItemDescription())
                .price(request.getItemPrice())
                .type(request.getItemType())
                .isListed(request.getIsListed())
                .build();
        itemRepository.save(item);
    }

    /**
     * 아이템을 Soft Delete 처리합니다.
     * itemKey에 해당하는 아이템을 조회하여 isActive 값을 false로 설정합니다.
     *
     * @param itemId 삭제할 아이템의 고유 키
     */
    @Transactional
    public void softDeleteItem(Long itemId) {

        Item item = itemService.getItemById(itemId)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_ITEM));
        item.setIsActive(false);

        List<MemberItem> memberItems = memberItemService.findAllByItem(item);
        memberItems.forEach((memberItem) -> {memberItem.setIsActive(false);});
    }
}
