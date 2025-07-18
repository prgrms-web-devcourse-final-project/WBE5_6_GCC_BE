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

}
