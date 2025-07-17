package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.member.domain.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QMember member = QMember.member;

    @Override
    public boolean isEmailVerified(String email) {
        return Boolean.TRUE.equals(queryFactory
            .select(member.isVerified)
            .from(member)
            .where(member.email.equalsIgnoreCase(email))
            .fetchOne());
    }

    @Override
    public void softDropMember(String userEmail) {
        queryFactory
            .update(member)
            .set(member.isActive, false)
            .where(member.email.eq(userEmail))
            .execute();
    }
}
