package com.honlife.core.app.model.item.service;

import com.honlife.core.app.controller.admin.item.payload.AdminCreateItemRequest;
import com.honlife.core.app.controller.admin.item.payload.AdminItemRequest;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.item.dto.ItemDTO;
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
     * @param itemId 수정 대상 아이템의 고유 id
     * @param request 수정할 값이 담긴 요청 DTO (이름, 설명, 가격, 타입)
     *
     * <p><b>[설명]</b></p>
     * - itemI로 아이템을 조회한 후, 전달받은 값으로 필드를 갱신합니다.
     * - 수정 대상 필드: name, description, price, type ,key
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

        if(request.getKey() != null && !request.getKey().equals(item.getKey())){
            // 해당 key가 이미 다른 아이템에서 사용 중이라면 중복
            if(itemService.itemKeyExists(request.getKey())){
                throw new CommonException(ResponseCode.DUPLICATE_ITEM_KEY);
            }
            item.setKey(request.getKey());
        }
        if (request.getIsListed() != null) {
            item.setIsListed(request.getIsListed());
        }
    }

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
                        .itemKey(item.getKey())
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

        item.setKey(item.getKey()+"_old");

        item.setIsActive(false);

        List<MemberItem> memberItems = memberItemService.findAllByItem(item);
        memberItems.forEach((memberItem) -> {memberItem.setIsActive(false);});
    }
}
