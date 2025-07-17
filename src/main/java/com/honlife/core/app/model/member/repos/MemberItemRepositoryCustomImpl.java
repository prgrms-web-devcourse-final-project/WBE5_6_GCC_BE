package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.domain.QItem;
import com.honlife.core.app.model.member.domain.QMemberItem;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberItemRepositoryCustomImpl implements MemberItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QMemberItem memberItem = QMemberItem.memberItem;
    QItem item = QItem.item;

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

    @Override
    public List<Tuple> findEquippedMemberItems(Long memberId) {
        return queryFactory
            .select(memberItem, item)
            .from(memberItem)
            .join(item).on(memberItem.item.id.eq(item.id))
            .where(
                memberItem.member.id.eq(memberId),
                memberItem.isEquipped.isTrue()
            )
            .fetch();
    }
}
