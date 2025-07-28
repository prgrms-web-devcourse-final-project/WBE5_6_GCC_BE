package com.honlife.core.app.model.item.repos;

import com.honlife.core.app.model.item.code.ItemType;
import com.querydsl.core.Tuple;
import java.util.List;

public interface ItemRepositoryCustom {
    List<Tuple> findItemsWithOwnership(Long memberId, ItemType itemType);
    Tuple findItemWithOwnership(Long itemId, Long memberId);
}