package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.badge.domain.QBadge;
import com.honlife.core.app.model.member.domain.MemberBadge;
import com.honlife.core.app.model.member.domain.QMemberBadge;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberBadgeRepositoryCustomImpl implements MemberBadgeRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QBadge badge = QBadge.badge;
    QMemberBadge memberBadge = QMemberBadge.memberBadge;

    @Override
    public void softDropByMemberId(Long memberId) {
        queryFactory
            .update(memberBadge)
            .set(memberBadge.isActive, false)
            .where(memberBadge.member.id.eq(memberId))
            .execute();
    }

    @Override
    public Optional<MemberBadge> findByMemberIsEquipped(String userEmail, boolean IsEquipped) {

        return Optional.ofNullable(queryFactory
            .select(memberBadge)
            .from(memberBadge)
            .leftJoin(memberBadge.badge, badge).fetchJoin()
            .where((memberBadge.member.email.eq(userEmail))
                .and(memberBadge.isEquipped.eq(IsEquipped))
            ).fetchOne());

    }
}
