package com.honlife.core.app.model.oauth2.service;

import com.honlife.core.app.model.auth.code.Role;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.oauth2.domain.SocialAccount;
import com.honlife.core.app.model.oauth2.dto.GoogleUserDetails;
import com.honlife.core.app.model.oauth2.dto.KakaoUserDetails;
import com.honlife.core.app.model.oauth2.dto.OAuth2UserInfo;
import com.honlife.core.infra.oauth2.CustomOAuth2UserDetails;
import com.honlife.core.app.model.oauth2.repos.SocialAccountRepository; // OAuth2AccessTokenRepository 임포트
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
    private final SocialAccountRepository socialAccountRepository; // 주입

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        // OAuth 제공자의 Access Token 및 provider 가져오기 및 저장/업데이트
        String oauthAccessToken = userRequest.getAccessToken().getTokenValue();
        Instant expiresAt = userRequest.getAccessToken().getExpiresAt();
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

        // 기존 회원의 정보를 찾고, 존재한다면 해당 회원 정보 사용, 신규라면 새로운 회원 저장

        Member member = memberRepository.findByEmailIgnoreCase(email);
        // 기존 회원이 아닌 경우
        if(member == null) {
            Member newMember;
            // 새로운 회원 정보 생성
            newMember = Member.builder()
                .email(email)
                .name(name)
                .nickname(name)
                .role(Role.ROLE_USER)
                .isVerified(true)
                .build();
            memberRepository.save(newMember);
            isNewMember = true;
            member = newMember;
            log.info("[CustomOAuth2UserService] Save member : {}", newMember);
        } else {
            isNewMember = false;
        }

        // 회원의 소셜계정 정보 업데이트
        if (oauthAccessToken != null) {
            Optional<SocialAccount> socialAccount = socialAccountRepository.findByMemberAndProvider(member, provider);

            if (socialAccount.isPresent()) {
                // 기존 토큰이 있으면 업데이트
                SocialAccount existingSocialAccount = socialAccount.get();
                existingSocialAccount.setAccessToken(oauthAccessToken);
                existingSocialAccount.setExpiryDate(expiresAt);
                socialAccountRepository.save(existingSocialAccount);
                log.info("CustomOAuth2UserService :: Updated existing OAuth2 account for member: {}", existingSocialAccount.getMember().getId());
            } else {
                // 없으면 새로 저장
                SocialAccount newSocialAccount = new SocialAccount();
                newSocialAccount = SocialAccount.builder()
                    .member(member)
                    .providerId(providerId)
                    .provider(provider)
                    .accessToken(oauthAccessToken)
                    .expiryDate(expiresAt)
                    .build();
                socialAccountRepository.save(newSocialAccount);
                log.info("CustomOAuth2UserService :: Saved new OAuth2 account: {}", newSocialAccount.getMember().getId());
            }
        } else {
            log.warn("CustomOAuth2UserService :: No Access Token received for provider: {}", provider);
        }

        return new CustomOAuth2UserDetails(member, oAuth2User.getAttributes(), isNewMember);
    }
}
