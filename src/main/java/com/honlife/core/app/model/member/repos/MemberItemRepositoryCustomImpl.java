package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.member.domain.QMemberItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberItemRepositoryCustomImpl implements MemberItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    QMemberItem memberItem = QMemberItem.memberItem;

    @Override
    public void softDropByMemberId(Long memberId) {

        queryFactory
            .update(memberItem)
            .set(memberItem.isActive, false)
            .where(memberItem.member.id.eq(memberId))
            .execute();

    }
}
