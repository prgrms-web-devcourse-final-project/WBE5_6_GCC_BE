package com.honlife.core.app.model.member.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberQuest;


public interface MemberQuestRepository extends JpaRepository<MemberQuest, Long>, MemberQuestRepositoryCustom {

    MemberQuest findFirstByMember(Member member);

}
