package com.honlife.core.app.model.item.repos;

import com.honlife.core.app.model.item.code.ItemType;
import com.querydsl.core.Tuple;
import java.util.List;

public interface ItemDslRepository {
    List<Tuple> findItemsWithOwnership(Long memberId, ItemType itemType);
    Tuple findItemWithOwnership(String itemKey, Long memberId);
}