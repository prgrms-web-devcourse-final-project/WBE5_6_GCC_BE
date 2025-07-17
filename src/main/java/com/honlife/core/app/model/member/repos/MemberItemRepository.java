package com.honlife.core.app.model.member.repos;


import com.honlife.core.app.controller.member.payload.MemberItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MemberItemRepository extends JpaRepository<MemberItem, Long> {

    MemberItem findFirstByMember(Member member);

    MemberItem findFirstByItem(Item item);

    // 보유 아이템 전체 (타입 조건 optional)
    @Query("""
    SELECT new com.honlife.core.app.controller.member.payload.MemberItemResponse(
        mi.item.itemKey,
        mi.item.name,
        mi.item.type,
        mi.item.description,
        mi.isEquipped
    )
    FROM MemberItem mi
    WHERE mi.member.id = :memberId
      AND (:itemType IS NULL OR mi.item.type = :itemType)
""")
    List<MemberItemResponse> findItemsByMemberId(@Param("memberId") Long memberId, @Param("itemType") ItemType itemType);

    // 장착 아이템만
    @Query("""
    SELECT new com.honlife.core.app.controller.member.payload.MemberItemResponse(
        mi.item.itemKey,
        mi.item.name,
        mi.item.type,
        mi.item.description,
        mi.isEquipped
    )
    FROM MemberItem mi
    WHERE mi.member.id = :memberId AND mi.isEquipped = true
""")
    List<MemberItemResponse> findEquippedItemsByMemberId(@Param("memberId") Long memberId);
}
