package com.honlife.core.app.model.member.repos;

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
    @Query("""
    SELECT mi.item.id
    FROM MemberItem mi
    JOIN mi.member m
    WHERE m.email = :email
""")
    List<Long> findItemsByMemberId(@Param("email") String email);

    // memberId와 ItemId를 통해 아이템 보유여부 확인
    @Query("""
    SELECT CASE WHEN COUNT(mi) > 0 THEN true ELSE false END
    FROM MemberItem mi
    WHERE mi.member.id = :memberId AND mi.item.id = :itemId
""")
    Boolean existsByMemberIdAndItemId(@Param("memberId") Long memberId, @Param("itemId") Long itemId);
}
