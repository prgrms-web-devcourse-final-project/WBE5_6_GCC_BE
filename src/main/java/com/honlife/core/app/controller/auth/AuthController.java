package com.honlife.core.app.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.controller.auth.payload.LoginRequest;
import com.honlife.core.app.controller.auth.payload.TokenResponse;
import com.honlife.core.app.model.auth.AuthService;
import com.honlife.core.app.model.auth.code.AuthToken;
import com.honlife.core.app.model.auth.dto.TokenDto;
import com.honlife.core.infra.auth.jwt.TokenCookieFactory;
import com.honlife.core.infra.response.CommonApiResponse;

@Tag(name="인증", description = "로그인 및 인증 관련 API입니다.")
@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final AuthService authService;


    /**
     * 로그인 처리 매서드
     * @param loginRequest
     * @param response
     * @return
     */
    @PostMapping("/signin")
    @Operation(summary = "로그인", description = "로그인 요청을 처리합니다.")
    public ResponseEntity<CommonApiResponse<TokenResponse>> login(
        @RequestBody LoginRequest loginRequest,
        HttpServletResponse response
    ) {
        TokenDto tokenDto = authService.signin(loginRequest);
        
        ResponseCookie accessToken = TokenCookieFactory.create(AuthToken.ACCESS_TOKEN.name(),
            tokenDto.getAccessToken(), tokenDto.getExpiresIn());
        ResponseCookie refreshToken = TokenCookieFactory.create(AuthToken.REFRESH_TOKEN.name(),
            tokenDto.getRefreshToken(), tokenDto.getExpiresIn());
        
        response.addHeader("Set-Cookie", accessToken.toString());
        response.addHeader("Set-Cookie", refreshToken.toString());
        
        return ResponseEntity.ok(CommonApiResponse.success(TokenResponse.builder().
                                                         accessToken(tokenDto.getAccessToken())
                                                         .grantType(tokenDto.getGrantType())
                                                         .expiresIn(tokenDto.getExpiresIn())
                                                         .build()));
    }
}
