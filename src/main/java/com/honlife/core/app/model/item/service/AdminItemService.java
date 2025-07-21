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
        if(item == null || item.getIsActive() == false) {
            // 해당 예외는 서버에 요청한 리소스가 존재하지 않는다”*는 의미 입니다!
            // 따라서 NOT_FOUND 를 사용 하였습니다.
            throw new CommonException(ResponseCode.NOT_FOUND_ITEM);
        }
        item.setIsActive(false);

        List<MemberItem> memberItems = memberItemService.findAllByItem(item);
        memberItems.forEach((memberItem) -> {memberItem.setIsActive(false);});
    }
}