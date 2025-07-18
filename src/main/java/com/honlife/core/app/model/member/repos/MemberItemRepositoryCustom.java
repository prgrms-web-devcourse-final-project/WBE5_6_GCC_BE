package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.item.code.ItemType;
import com.querydsl.core.Tuple;

import java.util.List;

public interface MemberItemRepositoryCustom {

    /**
     * 멤버 아이디를 통해 멤버 아이템을 소프트 드랍하는 메소드
     * @param memberId 멤버 식별 아이디
     */
    void softDropByMemberId(Long memberId);
    List<Tuple> findMemberItems(Long memberId, ItemType itemType);
}
