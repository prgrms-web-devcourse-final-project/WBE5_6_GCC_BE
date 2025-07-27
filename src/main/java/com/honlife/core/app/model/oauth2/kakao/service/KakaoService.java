package com.honlife.core.app.model.oauth2.kakao.service;

import com.honlife.core.app.model.auth.AuthService;
import com.honlife.core.app.model.auth.code.Role;
import com.honlife.core.app.model.auth.dto.TokenDto;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.oauth2.code.Provider;
import com.honlife.core.app.model.oauth2.kakao.dto.KakaoProfileDTO;
import com.honlife.core.app.model.oauth2.kakao.dto.KakaoTokenDTO;
import com.honlife.core.infra.oauth2.KakaoClient;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoService {

    private final KakaoClient kakaoClient;
    private final MemberRepository memberRepository;
    private final AuthService authService;

    /**
     * 인가 코드를 받아 카카오 로그인을 처리하고, 서비스의 JWT를 발급하는 메인 메소드
     * @param accessCode 카카오로부터 받은 인가 코드
     * @return TokenDto 서비스의 Access Token과 Refresh Token
     */
    public TokenDto kakaoLogin(String accessCode) {
        KakaoTokenDTO tokenDTO = kakaoClient.getAccessToken(accessCode);

        KakaoProfileDTO profileDTO = kakaoClient.getUserInfo(tokenDTO.getAccess_token());

        Member member = findOrCreateMember(profileDTO);
        String roles = "ROLE_" + member.getRole().name();

        return authService.processTokenSignin(member.getProviderId(), roles);
    }

    /**
     * 카카오로부터 건네 받은 프로필 내용을 기반으로, 기존에 로그인 한 사용자라면 사용자 정보 반환,<br>
     * 아니라면 새로운 사용자 저장.
     * @param profileDTO
     * @return
     */
    private Member findOrCreateMember(KakaoProfileDTO profileDTO) {
        Provider provider = Provider.KAKAO;
        String providerId = String.valueOf(profileDTO.getId());
        String email = (profileDTO.getKakaoAccount() != null && profileDTO.getKakaoAccount().getEmail() != null)
                            ? profileDTO.getKakaoAccount().getEmail()
                            : null;
        String nickname = profileDTO.getKakaoAccount().getProfile().getNickname();

        Optional<Member> member = memberRepository.findByProviderAndProviderId(provider, providerId);

        if (member.isPresent()) {
            return member.get();
        } else {
            Member newMember = Member.builder()
                .role(Role.ROLE_USER)
                .email(email)
                .password(null)
                .provider(provider)
                .providerId(providerId)
                .name(nickname)
                .isVerified(true)
                .build();
            memberRepository.save(newMember);
            return newMember;
        }
    }
}
