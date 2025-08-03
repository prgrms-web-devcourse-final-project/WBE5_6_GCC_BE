package com.honlife.core.infra.oauth2.service;

import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.infra.oauth2.domain.SocialAccount;
import com.honlife.core.infra.oauth2.repos.SocialAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoUnlinkService {

    private final RestTemplate restTemplate;
    private final SocialAccountRepository socialAccountRepository;

    // 회원 탈퇴 시 카카오 연동 해제
    public void unlink(Member member) {

        Optional<SocialAccount> oauthAccessTokenOptional = socialAccountRepository.findByMemberAndProvider(member, "kakao");

        if (oauthAccessTokenOptional.isEmpty()) {
            throw new RuntimeException("카카오 Access Token을 찾을 수 없습니다.");
        }

        SocialAccount oauthAccessToken = oauthAccessTokenOptional.get();
        String accessTokenValue = oauthAccessToken.getAccessToken();

        // 카카오 unlink API 호출 (Access Token 사용)
        String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessTokenValue);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>("", headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(unlinkUrl, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("카카오 연결 해제 실패: " + response.getBody());
            }
            // 연동 해제 성공 시, DB에서 Access Token 정보 삭제
            socialAccountRepository.delete(oauthAccessToken);
            // 회원 정보 삭제 (필요시)
            // memberRepository.delete(member);
        } catch (Exception e) {
            throw new RuntimeException("카카오 연결 해제 중 오류 발생", e);
        }
    }
}
