package com.honlife.core.app.controller.auth;

import com.honlife.core.app.controller.auth.payload.SignupRequest;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Operation(summary = "로그인", description = "로그인 요청을 처리합니다. JSON Request 필요.")
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
     * @param signupRequest
     * @return
     */
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입 요청을 처리합니다. 요청시 이메일로 인증번호를 전송합니다.<br>JSON Request 필요.")
    public ResponseEntity<CommonApiResponse<Void>> signup(
        @RequestBody SignupRequest signupRequest
    ) {
        //TODO: API 요청시 이메일로 인증번호 보내는 로직 추가 필요
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 로그아웃에 대한 Swagger 문서 작성을 위해 추가하였습니다. 개발단계에서 삭제하세요.
     */
    @Operation(summary = "로그아웃", description = "쿠키 기반 JWT 로그아웃을 수행합니다. 실제 처리는 필터에서 이루어집니다.")
    @ApiResponse(responseCode = "302", description = "로그아웃 후 리디렉션됩니다.")
    @PostMapping("/logout")
    public void logout() {}

    /**
     * 이메일 인증 처리 API
     * @param verifyCode 이메일로 전송된 인증 코드입니다.
     * @return
     */
    //TODO: Session 처리 필요한지 고민해보기, 인증로직 이메일 + 코드로 할건지 다른 방법으로 인증하고 코드만으로 비교할건지 고민해보기
    @PostMapping("/email/verify/{code}")
    @Operation(summary = "이메일 인증", description = "이메일 인증 요청을 처리합니다. 이메일로 전송된 코드가 올바른지 검사합니다.<br>*로직이 정해지지 않았습니다. 추후 변동 가능이 있습니다.*")
    public ResponseEntity<CommonApiResponse<Void>> verifyEmail(
        @PathVariable(name = "code")
        @Schema(description = "인증 코드", example = "12345") final String verifyCode
    ) {
        if(verifyCode.equals("12345")) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        return ResponseEntity.status(ResponseCode.INVALID_CODE.status())
            .body(CommonApiResponse.error(ResponseCode.INVALID_CODE));
    }

    /**
     * 중복 확인 처리 API, email 또는 nickname 둘 중 하나는 입력되어야 합니다.
     * @param email 사용자의 이메일
     * @param nickname 사용자의 닉네임
     * @return {@link CommonApiResponse}의 data에 중복여부를 담아 반환합니다.
     */
    @PostMapping("/check")
    @Operation(summary = "중복 검사", description = "이메일 또는 닉네임의 중복 여부를 검사합니다.<br>이메일 또는 닉네임만 param으로 받습니다. 둘 모두가 들어오거나 둘 모두 없는 경우 Bad Request를 응답합니다.")
    public ResponseEntity<CommonApiResponse<Map<String, Boolean>>> isEmailDuplicated(
        @RequestParam(name = "email", required = false)
        @Schema(description = "이메일", example = "user01@test.com") final String email,
        @RequestParam(name = "nickname", required = false)
        @Schema(description = "닉네임", example = "닉네임1") final String nickname
    ) {
        if(email != null && nickname == null){
            if(email.equals("user01@test.com")) {
                return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", true)));
            } else return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", false)));
        } else if (email == null && nickname != null) {
            if(nickname.equals("닉네임1")){
                return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", true)));
            } else return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", false)));
        }
        return ResponseEntity.badRequest().body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));

    }
}
