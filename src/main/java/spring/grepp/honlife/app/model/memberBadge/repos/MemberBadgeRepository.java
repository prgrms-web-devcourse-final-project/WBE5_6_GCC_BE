package spring.grepp.honlife.app.model.memberBadge.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.badge.domain.Badge;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.memberBadge.domain.MemberBadge;


public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Integer> {

    MemberBadge findFirstByMember(Member member);

    MemberBadge findFirstByBadge(Badge badge);

}
