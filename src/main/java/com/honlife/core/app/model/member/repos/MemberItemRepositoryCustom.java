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

    /**
     * 맴버 아이디를 통해 맴버가 보유하고 있는 아이템정보를 찾는 메소드
     * Soft delete 처리된 아이템(isActive = false)은 제외되며,
     * itemType이 지정된 경우 해당 타입에 해당하는 아이템만 조회합니다.
     * @param memberId 맴버 식별 아이디
     * @param itemType itemType 식별
     * @return
     */
    List<Tuple> findMemberItems(Long memberId, ItemType itemType);
}
