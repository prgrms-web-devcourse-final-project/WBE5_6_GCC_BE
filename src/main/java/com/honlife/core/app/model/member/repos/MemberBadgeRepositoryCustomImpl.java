package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.member.domain.QMemberBadge;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberBadgeRepositoryCustomImpl implements MemberBadgeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    QMemberBadge memberBadge = QMemberBadge.memberBadge;

    @Override
    public void softDropByMemberId(Long memberId) {
        queryFactory
            .update(memberBadge)
            .set(memberBadge.isActive, false)
            .where(memberBadge.member.id.eq(memberId))
            .execute();
    }
}
