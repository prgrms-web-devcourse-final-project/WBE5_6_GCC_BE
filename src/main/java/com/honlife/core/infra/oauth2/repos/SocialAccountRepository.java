package com.honlife.core.infra.oauth2.repos;

import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.infra.oauth2.domain.SocialAccount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> { // 인터페이스명 및 제네릭 변경

    // Member와 Provider로 Access Token 조회
    Optional<SocialAccount> findByMemberAndProvider(Member member, String provider);

    // Member와 Provider로 Access Token 삭제
    void deleteByMemberAndProvider(Member member, String provider);

    Long member(Member member);

    List<SocialAccount> findAllByMemberId(Long memberId);

    List<SocialAccount> findAllByMember(Member member);
}