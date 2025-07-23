package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.member.domain.MemberWeeklyQuest;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.Member;


public interface MemberQuestRepository extends JpaRepository<MemberWeeklyQuest, Long>, MemberQuestRepositoryCustom {

    MemberWeeklyQuest findFirstByMember(Member member);

    /**
     * 해당 멤버와 연관된 첫번째 멤버퀘스트를 조회
     * @param member
     * @param isActive
     * @return {@link MemberWeeklyQuest}
     */
    MemberWeeklyQuest findFirstByMemberAndIsActive(Member member, Boolean isActive);
}
