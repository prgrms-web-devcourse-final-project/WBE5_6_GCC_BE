package com.honlife.core.app.model.oauth2.repos;

import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.oauth2.domain.OAuth2AccessToken; // 임포트 변경
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuth2AccessTokenRepository extends JpaRepository<OAuth2AccessToken, Long> { // 인터페이스명 및 제네릭 변경

    // Member와 Provider로 Access Token 조회
    Optional<OAuth2AccessToken> findByMemberAndProvider(Member member, String provider);

    // Member와 Provider로 Access Token 삭제
    void deleteByMemberAndProvider(Member member, String provider);
}