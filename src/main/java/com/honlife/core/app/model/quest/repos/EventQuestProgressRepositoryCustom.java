package com.honlife.core.app.model.quest.repos;

public interface EventQuestProgressRepositoryCustom {

    void softDropByMemberId(Long memberId);

    void softDropByEventId(Long eventId);

}
