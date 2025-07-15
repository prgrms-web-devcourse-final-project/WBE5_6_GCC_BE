package com.honlife.core.app.controller.auth;

import com.honlife.core.app.controller.auth.payload.SignupRequest;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final MemberService memberService;


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
    public ResponseEntity<CommonApiResponse<Void>> signup(
        @RequestBody SignupRequest signupRequest
    ) {
        String userEmail = signupRequest.getEmail();
        if(memberService.isEmailExists(userEmail)) {        // 이미 존재하는 계정정보인지 확인
            if(memberService.isEmailVerified(userEmail)) {  // 인증이 완료된 계정인지 확인
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CommonApiResponse.error(ResponseCode.CONFLICT_EXIST_MEMBER));
            }
            // 회원가입을 재시도하는 익명사용자의 경우에, 기존에 저장된 정보를 업데이트
            memberService.updateNotVerifiedMember(signupRequest);
            return ResponseEntity.ok(CommonApiResponse.noContent());
        };
        // 신규 회원의 경우에 새로운 정보 저장
        memberService.saveNotVerifiedMember(signupRequest);
        // TODO: 인증코드 전송 로직 추가
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
    //TODO: Redis를 활용한 인증 방식 고민해보기. (Redis에 이메일과 인증번호를 저장해 두는 방식. TTL설정이 가능하기에 3분내 입력같은 기능 구현 가능)
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
    public ResponseEntity<CommonApiResponse<Map<String, Boolean>>> isEmailDuplicated(
        @RequestParam(name = "email", required = false) final String email,
        @RequestParam(name = "nickname", required = false) final String nickname
    ) {
        if(email != null && nickname == null){  // 이메일 중복 검사
            if(memberService.isEmailExists(email))
                return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", true)));
            else
                return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", false)));
        } else if (email == null && nickname != null) { // 닉네임 중복검사
            if(memberService.isNicknameExists(nickname))
                return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", true)));
            else
                return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", false)));
        }
        return ResponseEntity.badRequest().body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));

    }
}
