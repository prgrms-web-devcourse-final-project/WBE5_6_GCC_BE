package com.honlife.core.app.model.oauth2.domain;

import com.honlife.core.app.model.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialAccount {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
        name = "social_account_sequence",
        sequenceName = "social_account_sequence",
        allocationSize = 1,
        initialValue = 10000
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "social_account_sequence"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    @Column(length = 500) // Access Token 값
    private String accessToken; // 필드명 변경

    @Column
    private Instant expiryDate; // 토큰 만료 일시

//    // 토큰 값 업데이트 메소드
//    public void updateAccessToken(String newAccessTokenValue, Instant newExpiryDate) { // 메소드명 변경
//        this.accessTokenValue = newAccessTokenValue;
//        this.expiryDate = newExpiryDate;
//    }
}
