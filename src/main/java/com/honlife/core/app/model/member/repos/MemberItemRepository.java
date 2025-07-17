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

    // memberId를 통해 member가 보유하고있는 Item_id List 형식으로 반환
    @Query("SELECT mi.item.id FROM MemberItem mi WHERE mi.member.id = :memberId")
    List<Long> findItemsByMemberId(Long memberId);

    // memberId와 ItemId를 통해 아이템 보유여부 확인
    Boolean existsByMemberIdAndItemId(Long memberId, Long itemId);

    // 회원 + 아이템 타입 기준 조회
    @Query("""
    SELECT mi
    FROM MemberItem mi
    JOIN FETCH mi.item i
    WHERE mi.member.email = :email AND i.type = :type
""")
    List<MemberItem> findByMemberEmailAndItemType(@Param("email") String email, @Param("type") ItemType type);

    // 회원의 전체 아이템 조회
    @Query("""
    SELECT mi
    FROM MemberItem mi
    JOIN FETCH mi.item
    WHERE mi.member.email = :email
""")
    List<MemberItem> findByMemberEmail(@Param("email") String email);

    // 보유 아이템 전체 (타입 조건 optional)
    @Query("""
    SELECT new com.honlife.core.app.controller.item.payload.MemberItemResponse(
        mi.item.itemKey,
        mi.item.name,
        mi.item.type,
        mi.item.itemDescription,
        mi.isEquipped
    )
    FROM MemberItem mi
    WHERE mi.member.id = :memberId
      AND (:itemType IS NULL OR mi.item.type = :itemType)
""")
    List<MemberItemResponse> findItemsByMemberId(@Param("memberId") Long memberId, @Param("itemType") ItemType itemType);

    // 장착 아이템만
    @Query("""
    SELECT new com.honlife.core.app.controller.item.payload.MemberItemResponse(
        mi.item.itemKey,
        mi.item.name,
        mi.item.type,
        mi.item.itemDescription,
        mi.isEquipped
    )
    FROM MemberItem mi
    WHERE mi.member.id = :memberId AND mi.isEquipped = true
""")
    List<MemberItemResponse> findEquippedItemsByMemberId(@Param("memberId") Long memberId);
}
