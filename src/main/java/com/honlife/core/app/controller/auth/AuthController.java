package com.honlife.core.app.controller.auth;

import com.honlife.core.app.controller.auth.payload.DuplicationCheckRequest;
import com.honlife.core.app.controller.auth.payload.SignupRequest;
import com.honlife.core.app.controller.auth.payload.VerifyCodeRequestTest;
import com.honlife.core.app.controller.auth.payload.VerifyEmailRequest;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@Tag(name="✅ [일반] 인증", description = "로그인 및 인증 관련 API입니다.")
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
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
    @Operation(summary = "✅ 로그인", description = "로그인 요청을 처리합니다.<br>"
        + "로그인에 성공한 경우, 비동기로 로그인 로그에 로그인 기록을 저장합니다.")
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

    /**
     * 회원가입 처리 API
     *
     * @param signupRequest
     * @return
     */
    @PostMapping("/signup")
    @Operation(summary = "✅ 회원가입", description = "회원가입 요청을 처리합니다. <br>"
        + "이미 존재하거나, 이메일이 있지만 계정이 비활성화 상태인 경우, <code>HttpsStatus.CONFLICT</code>를,<br>"
        + "그 외의 경우는 <code>HttpStatus.OK</code>를 반환합니다.<br>"
        + "요청시 이메일로 인증번호를 전송합니다.<br>"
        + "인증번호의 유효시간은 3분입니다.")
    public ResponseEntity<CommonApiResponse<ResponseCode>> signup(
        @RequestBody SignupRequest signupRequest
    ) {
        //TODO: API 요청시 이메일로 인증번호 보내는 로직 추가 필요
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonApiResponse.noContent());
    }

    /**
     * 로그아웃에 대한 Swagger 문서 작성을 위해 추가하였습니다. 개발단계에서 삭제하세요.
     */
    @Operation(summary = "✅ 로그아웃", description = "쿠키 기반 JWT 로그아웃을 수행합니다. 실제 처리는 필터에서 이루어집니다.")
    @ApiResponse(responseCode = "302", description = "로그아웃 후 리디렉션됩니다.")
    @PostMapping("/logout")
    public void logout() {}

    @PostMapping("/auth/email")
    @Operation(summary = "✅ 인증번호 발송", description = "입력받은 이메일로 인증코드를 발송합니다.<br>"
        + "<code>RequestBody</code>의 <code>code</code>가 <code>null</code>이 아닌경우, <code>BAD_REQUEST</code>를 응답합니다. ")
    public ResponseEntity<CommonApiResponse<Void>> sendVerifyCode(
        @RequestBody @Valid VerifyEmailRequest emailRequest
    ) {
        if(emailRequest.getEmail().equals("user01@test.com") && emailRequest.getCode() == null) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        return ResponseEntity.badRequest()
            .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }


    @PostMapping("/auth/code")
    @Operation(summary = "✅ 인증번호 검증", description = "사용자가 입력한 인증코드가 올바른지 확인합니다.<br>"
        + "올바르게 인증 된 경우, 사용자가 로그인된 것으로 간주하고, 토큰을 반환합니다.")
    public ResponseEntity<CommonApiResponse<TokenResponse>> verifyEmail(
        @RequestBody @Valid VerifyCodeRequestTest emailRequest,
        HttpServletResponse response
    ) {
        if(emailRequest.getEmail().equals("user01@test.com") && emailRequest.getCode().equals("12345")) {
            LoginRequest loginRequest = new LoginRequest("user01@test.com", "1111");
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
        } else if (!emailRequest.getCode().equals("12345")){
            return ResponseEntity.status(ResponseCode.INVALID_CODE.status()).body(CommonApiResponse.error(ResponseCode.INVALID_CODE));
        }
        return ResponseEntity.badRequest()
            .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }


    @PostMapping("/check")
    @Operation(summary = "✅ 중복 검사", description = "중복된 이메일 또는 닉네임이 존재하는지 확인합니다.<br>"
        + "이미 존재하는 이메일 또는 닉네임인 경우, <code>409(Conflict)</code>를 반환합니다.<br>"
        + "요청시 이메일 또는 닉네임 둘중 하나만 값이 있어야 하며, 둘다 null이거나 데이터를 가지고 있다면 <code>400(BadRequest)</code>를 반환합니다.")
    public ResponseEntity<CommonApiResponse<Map<String, Boolean>>> isEmailDuplicated(
        @RequestBody DuplicationCheckRequest checkRequest
    ) {
        String email = checkRequest.getEmail();
        String nickname = checkRequest.getNickname();
        if (email != null && nickname == null) {
            if(email.equals("user01@test.com")){
                return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", true)));
            } else {
                return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", false)));
            }
        } else if (email == null && nickname != null) {
            if(nickname.equals("닉네임1")){
                return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", true)));
            } else {
                return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", false)));
            }
        }
        return ResponseEntity.badRequest().body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }


    @Operation(summary = "✅ 소셜 로그인 링크", description = "소셜 로그인 버튼에 거시면 되는 로그인 주소입니다.<br>"
        + "provider는 kakao 또는 google 입니다.<br>"
        + "소셜로그인 성공시 최초 로그인 회원은 추가정보 입력 페이지로, 기존 회원은 홈으로 리다이렉트 됩니다.<br>"
        + "⚠️ Swagger는 아무런 동작도 하지 않습니다.")
    @GetMapping("/oauth2/authorization/{provider}")
    public void socialLogin(
        @PathVariable(name = "provider") String provider
    ) {

    }
}
