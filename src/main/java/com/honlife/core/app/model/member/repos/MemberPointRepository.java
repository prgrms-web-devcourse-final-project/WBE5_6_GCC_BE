package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.MemberPoint;


public interface MemberPointRepository extends JpaRepository<MemberPoint, Long> {

    MemberPoint findFirstByMember(Member member);

    boolean existsByMemberId(Long id);

}
