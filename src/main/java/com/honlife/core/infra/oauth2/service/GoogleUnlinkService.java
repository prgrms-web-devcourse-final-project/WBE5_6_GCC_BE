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
public class GoogleUnlinkService {

    private final RestTemplate restTemplate;
    private final SocialAccountRepository socialAccountRepository;

    // 회원 탈퇴 시 구글 연동 해제
    public void unlink(Member member) {

        Optional<SocialAccount> oauthAccessTokenOptional = socialAccountRepository.findByMemberAndProvider(member, "google");

        if (oauthAccessTokenOptional.isEmpty()) {
            throw new RuntimeException("구글 Access Token을 찾을 수 없습니다.");
        }

        SocialAccount oauthAccessToken = oauthAccessTokenOptional.get();
        String accessTokenValue = oauthAccessToken.getAccessToken();

        // 구글 revoke API 호출 (Access Token 사용)
        String revokeUrl = "https://oauth2.googleapis.com/revoke?token=" + accessTokenValue;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>("", headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(revokeUrl, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("구글 연결 해제 실패: " + response.getBody());
            }
            // 연동 해제 성공 시, DB에서 Access Token 정보 삭제
            socialAccountRepository.delete(oauthAccessToken);
            // 회원 정보 삭제 (필요시)
            // memberRepository.delete(member);
        } catch (Exception e) {
            throw new RuntimeException("구글 연결 해제 중 오류 발생", e);
        }
    }
}
