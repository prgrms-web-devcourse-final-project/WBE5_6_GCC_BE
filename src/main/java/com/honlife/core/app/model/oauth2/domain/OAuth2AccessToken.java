package com.honlife.core.app.model.oauth2.domain;

import com.honlife.core.app.model.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oauth2_access_token") // 테이블명 변경
public class OAuth2AccessToken { // 클래스명 변경

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String provider; // "kakao", "google" 등 OAuth 제공자

    @Column(nullable = false, length = 500) // Access Token 값
    private String accessTokenValue; // 필드명 변경

    @Column(nullable = false)
    private Instant expiryDate; // 토큰 만료 일시

    // 토큰 값 업데이트 메소드
    public void updateAccessToken(String newAccessTokenValue, Instant newExpiryDate) { // 메소드명 변경
        this.accessTokenValue = newAccessTokenValue;
        this.expiryDate = newExpiryDate;
    }
}