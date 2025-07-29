package com.honlife.core.app.model.quest.repos;

import com.honlife.core.app.model.quest.domain.QWeeklyQuestProgress;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WeeklyQuestProgressRepositoryCustomImpl implements
    WeeklyQuestProgressRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QWeeklyQuestProgress weeklyQuestProgress = QWeeklyQuestProgress.weeklyQuestProgress;


    @Override
    public void softDropByMemberId(Long memberId) {
        queryFactory
            .update(weeklyQuestProgress)
            .set(weeklyQuestProgress.isActive, false)
            .where(weeklyQuestProgress.member.id.eq(memberId))
            .execute();
    }

    @Override
    public void deactivateAllActiveWeeklyQuests() {
        queryFactory
            .update(weeklyQuestProgress)
            .set(weeklyQuestProgress.isActive, false)
            .where(weeklyQuestProgress.isActive.isTrue())
            .execute();
    }


}
