package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


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
    /**
     * memberId로 MemberPoint 조회
     *
     * @param memberId 사용자 ID
     * @return Optional<MemberPoint>
     */
    Optional<MemberPoint> findByMemberId(Long memberId);

    Optional<MemberPoint> findByMember_Email(String email);

    Optional<MemberPoint> findByMember_EmailAndIsActive(String memberEmail, Boolean isActive);
}
