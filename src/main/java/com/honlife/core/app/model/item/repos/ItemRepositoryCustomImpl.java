package com.honlife.core.app.model.item.repos;

import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.domain.QItem;
import com.honlife.core.app.model.member.domain.QMemberItem;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QItem item = QItem.item;
    QMemberItem mi = QMemberItem.memberItem;

    @Override
    public List<Tuple> findItemsWithOwnership(Long memberId, ItemType itemType) {
        return queryFactory
                .select(item, mi.id)
                .from(item)
                .leftJoin(mi).on(mi.item.id.eq(item.id).and(mi.member.id.eq(memberId)))
                .where(item.isActive.eq(true),
                       itemType != null ? item.type.eq(itemType) : null)
                .fetch();
    }

    @Override
    public Tuple findItemWithOwnership(String itemKey, Long memberId) {
        return queryFactory
                .select(item, mi.id)
                .from(item)
                .leftJoin(mi).on(mi.item.id.eq(item.id).and(mi.member.id.eq(memberId)))
                .where(item.key.eq(itemKey),
                       item.isActive.eq(true))
                .fetchOne();
    }
}
