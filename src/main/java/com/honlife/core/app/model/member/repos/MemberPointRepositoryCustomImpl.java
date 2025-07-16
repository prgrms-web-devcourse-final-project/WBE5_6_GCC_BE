package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.member.domain.QMemberPoint;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberPointRepositoryCustomImpl implements MemberPointRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    QMemberPoint memberPoint = QMemberPoint.memberPoint;

    @Override
    public void deleteByMemberId(Long memberId) {
        queryFactory
            .update(memberPoint)
            .set(memberPoint.isActive, false)
            .where(memberPoint.member.id.eq(memberId))
            .execute();
    }
}
