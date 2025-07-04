package spring.grepp.honlife.member_quest.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.member.domain.Member;
import spring.grepp.honlife.member_quest.domain.MemberQuest;


public interface MemberQuestRepository extends JpaRepository<MemberQuest, Integer> {

    MemberQuest findFirstByMember(Member member);

}
