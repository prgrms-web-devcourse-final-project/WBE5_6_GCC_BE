package com.honlife.core.app.model.oauth2.service;

import com.honlife.core.app.model.auth.code.Role;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.oauth2.dto.GoogleUserDetails;
import com.honlife.core.app.model.oauth2.dto.KakaoUserDetails;
import com.honlife.core.app.model.oauth2.dto.OAuth2UserInfo;
import com.honlife.core.infra.oauth2.CustomOAuth2UserDetails;
import com.honlife.core.app.model.oauth2.domain.OAuth2AccessToken; // OAuth2AccessToken 임포트
import com.honlife.core.app.model.oauth2.repos.OAuth2AccessTokenRepository; // OAuth2AccessTokenRepository 임포트
import java.time.Instant;
import java.util.Optional;
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
    private final OAuth2AccessTokenRepository oauth2AccessTokenRepository; // 주입

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        if(provider.equals("google")) {
            log.info("[OAuth2] Google Login");
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        } else if (provider.equals("kakao")) {
            log.info("[OAuth2] Kakao Login");
            oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();
        boolean isNewMember;

        Optional<Member> memberOptional = memberRepository.findByProviderId(providerId);
        Member member;
        if(memberOptional.isEmpty()) {
            member = Member.builder()
                .email(email)
                .name(name)
                .nickname(name)
                .provider(provider)
                .providerId(providerId)
                .role(Role.ROLE_USER)
                .isVerified(true)
                .build();
            memberRepository.save(member);
            isNewMember = true;
            log.info("[CustomOAuth2UserService] Save member : {}", member);
        } else {
            member = memberOptional.get();
            isNewMember = false;
            log.info("[CustomOAuth2UserService] Found member : {}", member);
        }

        // OAuth 제공자의 Access Token 가져오기 및 저장/업데이트
        String oauthAccessTokenValue = userRequest.getAccessToken().getTokenValue();
        Instant expiresAt = userRequest.getAccessToken().getExpiresAt();

        if (oauthAccessTokenValue != null) {
            Optional<OAuth2AccessToken> existingToken = oauth2AccessTokenRepository.findByMemberAndProvider(member, provider);

            if (existingToken.isPresent()) {
                // 기존 토큰이 있으면 업데이트
                OAuth2AccessToken token = existingToken.get();
                token.updateAccessToken(oauthAccessTokenValue, expiresAt);
                oauth2AccessTokenRepository.save(token);
                log.info("[CustomOAuth2UserService] Updated OAuth2 Access Token for member: {}", member.getId());
            } else {
                // 없으면 새로 저장
                OAuth2AccessToken newToken = OAuth2AccessToken.builder()
                        .member(member)
                        .provider(provider)
                        .accessTokenValue(oauthAccessTokenValue)
                        .expiryDate(expiresAt)
                        .build();
                oauth2AccessTokenRepository.save(newToken);
                log.info("[CustomOAuth2UserService] Saved new OAuth2 Access Token for member: {}", member.getId());
            }
        } else {
            log.warn("[CustomOAuth2UserService] No Access Token received for provider: {}", provider);
        }

        return new CustomOAuth2UserDetails(member, oAuth2User.getAttributes(), isNewMember);
    }
}
