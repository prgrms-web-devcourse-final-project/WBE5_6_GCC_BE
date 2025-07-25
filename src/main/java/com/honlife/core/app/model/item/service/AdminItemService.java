package com.honlife.core.app.model.item.service;

import com.honlife.core.app.controller.admin.item.payload.AdminItemRequest;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminItemService {

    private ItemService itemService;

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
    public void updateItem(String itemKey, AdminItemRequest request) {
        Item item = itemService.getItemByKey(itemKey)
                .orElseThrow(()->new CommonException(ResponseCode.NOT_FOUND_ITEM));
        item.setName(request.getItemName());
        item.setDescription(request.getItemDescription());
        item.setPrice(request.getPrice());
        item.setType(request.getItemType());

        if (request.getIsListed() != null) {
            item.setIsListed(request.getIsListed());
        }
    }
}
