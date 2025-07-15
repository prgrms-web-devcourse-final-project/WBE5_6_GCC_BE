package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberPointRepository extends JpaRepository<MemberPoint, Long> {

    MemberPoint findFirstByMember(Member member);

    boolean existsByMemberId(Long id);

    Optional<MemberPoint> findByMemberId(Long memberId);
}
