package com.honlife.core.app.model.member.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


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
}
