package spring.grepp.honlife.app.model.memberQuest.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.memberQuest.domain.MemberQuest;


public interface MemberQuestRepository extends JpaRepository<MemberQuest, Integer> {

    MemberQuest findFirstByMember(Member member);

}
