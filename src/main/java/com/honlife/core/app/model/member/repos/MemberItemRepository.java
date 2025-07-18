package com.honlife.core.app.model.member.repos;


import com.honlife.core.app.model.item.domain.Item;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberItemRepository extends JpaRepository<MemberItem, Long>, MemberItemRepositoryCustom {

    MemberItem findFirstByMember(Member member);

    MemberItem findFirstByItem(Item item);

}
