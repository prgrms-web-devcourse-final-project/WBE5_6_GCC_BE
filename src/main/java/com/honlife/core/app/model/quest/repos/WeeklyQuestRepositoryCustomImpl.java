package com.honlife.core.app.model.quest.repos;

import com.honlife.core.app.model.quest.domain.QWeeklyQuest;
import com.honlife.core.app.model.quest.domain.WeeklyQuest;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WeeklyQuestRepositoryCustomImpl implements WeeklyQuestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QWeeklyQuest weeklyQuest = QWeeklyQuest.weeklyQuest;

    @Override
    public List<WeeklyQuest> getRandomWeeklyQuest(int count) {
        return queryFactory
            .selectFrom(weeklyQuest)
            .where(weeklyQuest.isActive.isTrue())
            .orderBy(Expressions.numberTemplate(Double.class, "function('RANDOM')").asc())
            .limit(count)
            .fetch();
    }
}
