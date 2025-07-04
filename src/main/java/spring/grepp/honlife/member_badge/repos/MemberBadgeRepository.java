package spring.grepp.honlife.member_badge.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.badge.domain.Badge;
import spring.grepp.honlife.member.domain.Member;
import spring.grepp.honlife.member_badge.domain.MemberBadge;


public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Integer> {

    MemberBadge findFirstByMember(Member member);

    MemberBadge findFirstByBadge(Badge badge);

}
