package com.honlife.core.app.model.member.repos;

import java.util.List;

import com.honlife.core.app.model.item.code.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MemberItemRepository extends JpaRepository<MemberItem, Long>, MemberItemRepositoryCustom {

    MemberItem findFirstByMember(Member member);

    MemberItem findFirstByItem(Item item);

    // memberId와 ItemId를 통해 아이템 보유여부 확인
    @Query("""
    SELECT CASE WHEN COUNT(mi) > 0 THEN true ELSE false END
    FROM MemberItem mi
    WHERE mi.member.id = :memberId AND mi.item.id = :itemId
""")
    Boolean existsByMemberIdAndItemId(@Param("memberId") Long memberId, @Param("itemId") Long itemId);
    List<MemberItem> member(Member member);

    MemberItem findFirstByMemberAndIsActive(Member member, Boolean isActive);

    /**
     * 특정 회원이 장착 중인 아이템들 중, 타입이 일치하고 isEquipped=true인 항목 조회
     */
    List<MemberItem> findByMemberIdAndItemTypeAndIsEquippedTrue(Long memberId, ItemType type);

    /**
     * 특정 회원이 보유한 특정 아이템 조회
     */
    Optional<MemberItem> findByMemberIdAndItemId(Long memberId, Long itemId);
}
