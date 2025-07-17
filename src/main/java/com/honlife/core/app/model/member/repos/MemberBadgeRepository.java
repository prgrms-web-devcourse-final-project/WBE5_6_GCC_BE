package com.honlife.core.app.model.member.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.badge.domain.Badge;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberBadge;


public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long>,MemberBadgeRepositoryCustom {

    MemberBadge findFirstByMember(Member member);

    MemberBadge findFirstByBadge(Badge badge);

    /**
     * 해당 멤버와 연관된 첫번째 멤버 뱃지를 조회
     * @param member
     * @param isActive
     * @return {@link MemberBadge}
     */
    MemberBadge findFirstByMemberAndIsActive(Member member, Boolean isActive);
}
