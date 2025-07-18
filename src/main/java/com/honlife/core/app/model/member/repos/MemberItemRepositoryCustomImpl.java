package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.domain.QItem;
import com.honlife.core.app.model.member.domain.QMemberItem;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MemberItemRepositoryCustomImpl implements MemberItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QMemberItem memberItem = QMemberItem.memberItem;
    QItem item = QItem.item;

    @Override
    public void softDropByMemberId(Long memberId) {

        queryFactory
                .update(memberItem)
                .set(memberItem.isActive, false)
                .where(memberItem.member.id.eq(memberId))
                .execute();
    }
    @Override
    public List<Tuple> findMemberItems(Long memberId, ItemType itemType) {
        return queryFactory
            .select(memberItem, item)
            .from(memberItem)
            .join(item).on(memberItem.item.id.eq(item.id))
            .where(
                memberItem.member.id.eq(memberId),
                itemType != null ? item.type.eq(itemType) : null
            )
            .fetch();
    }
}
