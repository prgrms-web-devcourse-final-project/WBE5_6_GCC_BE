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

    // 사용자 email을 통해 MemberPoint 테이블 정보 반환
    @Query("""
    SELECT mp
    FROM MemberPoint mp
    WHERE mp.member.email = :email
""")
    Optional<MemberPoint> findPointByMemberEmail(@Param("email") String email);
}
