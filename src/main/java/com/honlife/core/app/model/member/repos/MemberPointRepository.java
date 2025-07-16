package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MemberPointRepository extends JpaRepository<MemberPoint, Long> {

    MemberPoint findFirstByMember(Member member);

    boolean existsByMemberId(Long id);

    /**
     * memberId로 MemberPoint 조회
     *
     * @param memberId 사용자 ID
     * @return Optional<MemberPoint>
     */
    Optional<MemberPoint> findByMemberId(Long memberId);
}
