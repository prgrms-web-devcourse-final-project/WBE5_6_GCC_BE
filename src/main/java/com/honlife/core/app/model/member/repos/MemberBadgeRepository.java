package com.honlife.core.app.model.member.repos;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.badge.domain.Badge;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberBadge;


public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long> {

    MemberBadge findFirstByMember(Member member);

    MemberBadge findFirstByBadge(Badge badge);

    /**
     * 특정 회원의 특정 배지 획득 정보 조회
     */
    Optional<MemberBadge> findByMemberAndBadge(Member member, Badge badge);

    /**
     * 특정 회원이 획득한 모든 배지 조회
     */
    List<MemberBadge> findByMember(Member member);

    /**
     * 특정 회원의 특정 배지 획득 여부 확인 (성능 최적화용)
     */
    boolean existsByMemberAndBadge(Member member, Badge badge);
}
