package com.honlife.core.infra.oauth2;

import com.honlife.core.app.model.oauth2.kakao.dto.KakaoProfileDTO;
import com.honlife.core.app.model.oauth2.kakao.dto.KakaoTokenDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoClient {

    private final RestTemplate restTemplate;

    @Value("${spring.kakao.auth.client}")
    private String clientId;

    @Value("${spring.kakao.auth.redirect}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    /**
     * 카카오 서버로부터 액세스 토큰을 받아오는 메소드
     * @param accessCode 프론트엔드에서 전달받은 인가 코드
     * @return KakaoTokenDTO 카카오 토큰 정보
     */
    public KakaoTokenDTO getAccessToken(String accessCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", accessCode);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenDTO> responseEntity = restTemplate.postForEntity(
            tokenUri, kakaoTokenRequest, KakaoTokenDTO.class);

        return responseEntity.getBody();
    }

    /**
     * 액세스 토큰을 사용하여 카카오 서버로부터 사용자 정보를 받아오는 메소드
     * @param accessToken 카카오로부터 발급받은 액세스 토큰
     * @return KakaoProfileDTO 카카오 사용자 정보
     */
    public KakaoProfileDTO getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<KakaoProfileDTO> responseEntity = restTemplate.postForEntity(
            userInfoUri,
            requestEntity,
            KakaoProfileDTO.class
        );

        return responseEntity.getBody();
    }
}
