package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.routine.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberQuest;


public interface MemberQuestRepository extends JpaRepository<MemberQuest, Long>, MemberQuestRepositoryCustom {

    MemberQuest findFirstByMember(Member member);

    /**
     * 해당 멤버와 연관된 첫번째 멤버퀘스트를 조회
     * @param member
     * @param isActive
     * @return {@link MemberQuest}
     */
    MemberQuest findFirstByMemberAndIsActive(Member member, Boolean isActive);
}
