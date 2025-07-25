package com.honlife.core.app.model.quest.repos;

import com.honlife.core.app.model.quest.domain.WeeklyQuestProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.Member;


public interface WeeklyQuestProgressRepository extends JpaRepository<WeeklyQuestProgress, Long>,
    WeeklyQuestProgressRepositoryCustom {

    WeeklyQuestProgress findFirstByMember(Member member);

    /**
     * 해당 멤버와 연관된 첫번째 멤버퀘스트를 조회
     * @param member
     * @param isActive
     * @return {@link WeeklyQuestProgress}
     */
    WeeklyQuestProgress findFirstByMemberAndIsActive(Member member, Boolean isActive);
}
