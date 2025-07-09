package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.MemberImage;


public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {

    MemberImage findFirstByMember(Member member);

    boolean existsByMemberId(Long id);

}
