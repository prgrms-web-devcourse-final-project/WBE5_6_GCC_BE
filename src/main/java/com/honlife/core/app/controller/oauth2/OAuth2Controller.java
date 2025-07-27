package com.honlife.core.app.controller.oauth2;

import com.honlife.core.app.controller.auth.payload.TokenResponse;
import com.honlife.core.app.model.auth.dto.TokenDto;
import com.honlife.core.app.model.oauth2.kakao.service.KakaoService;
import com.honlife.core.infra.response.CommonApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class OAuth2Controller {

    private final KakaoService kakaoService;

    /**
     * 카카오 로그인 콜백을 처리하는 API
     * 프론트엔드에서 카카오 로그인 성공 후, 리다이렉트되어 호출되는 엔드포인트입니다.
     * @param accessCode 카카오 서버가 리다이렉트 시 함께 전달해주는 인가 코드
     * @return ResponseEntity<CommonApiResponse<TokenDto>> 서비스의 JWT 토큰 정보
     */
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<CommonApiResponse<TokenResponse>> kakaoLogin(
        @RequestParam("code") String accessCode,
        HttpServletResponse response
    ) {
        TokenDto tokenDto = kakaoService.kakaoLogin(accessCode);

        return ResponseEntity.ok(CommonApiResponse.success(TokenResponse.getTokenResponse(tokenDto, response)));
    }
}
