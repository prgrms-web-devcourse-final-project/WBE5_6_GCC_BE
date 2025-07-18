package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.MemberPoint;


public interface MemberPointRepository extends JpaRepository<MemberPoint, Long>, MemberPointRepositoryCustom {

    MemberPoint findFirstByMember(Member member);

    boolean existsByMemberId(Long id);

    /**
     * 해당 멤버와 연관된 첫번째 멤버 포인트를 조회
     * @param member
     * @param isActive
     * @return {@link MemberPoint}
     */
    MemberPoint findFirstByMemberAndIsActive(Member member, Boolean isActive);
}
