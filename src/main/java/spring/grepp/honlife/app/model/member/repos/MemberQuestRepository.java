package spring.grepp.honlife.app.model.member.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.member.domain.MemberQuest;


public interface MemberQuestRepository extends JpaRepository<MemberQuest, Long> {

    MemberQuest findFirstByMember(Member member);

}
