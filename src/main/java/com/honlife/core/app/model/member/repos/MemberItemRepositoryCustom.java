package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.item.code.ItemType;
import com.querydsl.core.Tuple;

import java.util.List;

public interface MemberItemRepositoryCustom {
    List<Tuple> findMemberItems(Long memberId, ItemType itemType);
    List<Tuple> findEquippedMemberItems(Long memberId);
}
