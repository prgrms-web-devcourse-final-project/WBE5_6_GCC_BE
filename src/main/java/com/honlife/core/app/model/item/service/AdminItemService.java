package com.honlife.core.app.model.item.service;

import com.honlife.core.app.model.item.domain.Item;
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

    private final ItemService itemService;
    private final MemberItemService memberItemService;

    /**
     * 아이템을 Soft Delete 처리합니다.
     * itemKey에 해당하는 아이템을 조회하여 isActive 값을 false로 설정합니다.
     *
     * @param itemKey 삭제할 아이템의 고유 키
     */
    @Transactional
    public void softDeleteItem(String itemKey) {

        Item item = itemService.getItemByKey(itemKey);
        if(item == null) {
            throw new CommonException(ResponseCode.NOT_FOUND_ITEM);
        }
        item.setIsActive(false);

        List<MemberItem> memberItems = memberItemService.findAllByItem(item);
        memberItems.forEach((memberItem) -> {memberItem.setIsActive(false);});
    }

    /**
     * 아이템의 isListed 값을 수정합니다.
     * - itemKey로 해당 아이템을 조회하여, 존재하지 않으면 예외를 발생시킵니다.
     * - 존재할 경우 isListed 값을 변경합니다.
     *
     * @param itemKey   대상 아이템의 고유 키
     * @param isListed  변경할 노출 여부 값 (true: 노출, false: 숨김)
     */
    @Transactional
    public void updateListedStatus(String itemKey, Boolean isListed) {
        Item item = itemService.getItemByKey(itemKey);
        if(item == null) {
            throw new CommonException(ResponseCode.NOT_FOUND_ITEM);
        }
        item.setIsListed(isListed);
    }
}