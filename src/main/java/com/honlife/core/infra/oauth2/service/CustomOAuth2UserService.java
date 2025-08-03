package com.honlife.core.infra.oauth2.service;

import com.honlife.core.app.model.auth.code.Role;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.oauth2.CustomOAuth2UserDetails;
import com.honlife.core.infra.oauth2.domain.SocialAccount;
import com.honlife.core.infra.oauth2.dto.GoogleUserDetails;
import com.honlife.core.infra.oauth2.dto.KakaoUserDetails;
import com.honlife.core.infra.oauth2.dto.OAuth2UserInfo;
import com.honlife.core.infra.oauth2.repos.SocialAccountRepository;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final SocialAccountRepository socialAccountRepository; // 주입

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("loadUser() :: getAttributes : {}", oAuth2User.getAttributes());

        // OAuth 제공자의 Access Token 및 provider 가져오기 및 저장/업데이트
        String oauthAccessToken = userRequest.getAccessToken().getTokenValue();
        Instant expiresAt = userRequest.getAccessToken().getExpiresAt();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String requestUri = "/login/oauth2/code/" + provider;

        // 1. OAuth2UserInfo.createUserInfo 팩토리 메서드 사용
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.createUserInfo(requestUri, oAuth2User);

        // 2. 지원하지 않는 Provider 예외 처리
        if (oAuth2UserInfo == null) {
            throw new OAuth2AuthenticationException("loadUser() :: Unsupported provider: " + provider);
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        if (email == null) {
            // 소셜 로그인 시 이메일은 필수 정보임을 알리는 예외 발생
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider. Please check your consent settings.");
        }
        String name = oAuth2UserInfo.getName();
        boolean isNewMember = false;

        // 기존 회원의 정보를 찾고, 존재한다면 해당 회원 정보 사용, 신규라면 새로운 회원 저장

        Member member = memberRepository.findByEmailIgnoreCase(email);
        // 기존 회원이 아닌 경우
        if(member == null) {
            // 새로운 회원 정보 생성
            member = Member.builder()
                .email(email)
                .name(name)
                .nickname("USER_" + UUID.randomUUID())
                .role(Role.ROLE_USER)
                .isVerified(true)
                .build();
            memberRepository.save(member);
            isNewMember = true;
            log.info("loadUser() :: Save new member --- user_email : {}", member.getEmail());
        }

        SocialAccount socialAccount = socialAccountRepository.findByMemberAndProvider(member, provider).orElse(null);
        // 회원의 소셜계정 정보 업데이트
        if (oauthAccessToken != null) {
            if (socialAccount != null) {
                // 기존 토큰이 있으면 업데이트
                socialAccount.setAccessToken(oauthAccessToken);
                socialAccount.setExpiryDate(expiresAt);
                socialAccountRepository.save(socialAccount);
                log.info("loadUser() :: Updated existing OAuth2 account for member: {}", socialAccount.getMember().getId());
            } else {
                // 없으면 새로 저장
                socialAccount = SocialAccount.builder()
                    .member(member)
                    .providerId(providerId)
                    .provider(provider)
                    .accessToken(oauthAccessToken)
                    .expiryDate(expiresAt)
                    .build();
                socialAccountRepository.save(socialAccount);
                log.info("loadUser() :: Saved new OAuth2 account: {}", socialAccount.getMember().getId());
            }
        } else {
            log.warn("loadUser() :: No Access Token received for provider: {}", provider);
        }

        return new CustomOAuth2UserDetails(member, oAuth2User.getAttributes(), isNewMember);
    }
}