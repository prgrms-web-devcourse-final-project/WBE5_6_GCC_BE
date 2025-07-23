package com.honlife.core.app.model.quest.repos;

public interface WeeklyQuestProgressRepositoryCustom {

    /**
     * 멤버 아이디를 통해 멤버 퀘스트를 소프트 드랍하는 메소드
     * @param memberId 멤버 식별 아이디
     */
    void softDropByMemberId(Long memberId);
}
