package spring.grepp.honlife.app.model.member.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.badge.domain.Badge;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.member.domain.MemberBadge;


public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long> {

    MemberBadge findFirstByMember(Member member);

    MemberBadge findFirstByBadge(Badge badge);

}
