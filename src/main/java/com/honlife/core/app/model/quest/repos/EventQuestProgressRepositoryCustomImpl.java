package com.honlife.core.app.model.quest.repos;

import com.honlife.core.app.model.quest.domain.QEventQuestProgress;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventQuestProgressRepositoryCustomImpl implements EventQuestProgressRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QEventQuestProgress eventQuestProgress = QEventQuestProgress.eventQuestProgress;

    @Override
    public void softDropByMemberId(Long memberId) {
        queryFactory
            .update(eventQuestProgress)
            .set(eventQuestProgress.isActive, false)
            .where(eventQuestProgress.member.id.eq(memberId))
            .execute();
    }

    @Override
    public void softDropByEventId(Long eventId) {
        queryFactory
            .update(eventQuestProgress)
            .set(eventQuestProgress.isActive, false)
            .where(eventQuestProgress.eventQuest.id.eq(eventId))
            .execute();
    }
}
