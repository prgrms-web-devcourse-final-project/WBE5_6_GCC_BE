package com.honlife.core.app.model.quest.repos;

import com.honlife.core.app.model.member.domain.QMemberQuest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WeeklyQuestProgressRepositoryCustomImpl implements
    WeeklyQuestProgressRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QMemberQuest memberQuest = QMemberQuest.memberQuest;


    @Override
    public void softDropByMemberId(Long memberId) {
        queryFactory
            .update(memberQuest)
            .set(memberQuest.isActive, false)
            .where(memberQuest.member.id.eq(memberId))
            .execute();
    }
}
