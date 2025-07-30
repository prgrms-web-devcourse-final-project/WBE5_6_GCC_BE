package com.honlife.core.app.controller.auth;

import com.honlife.core.app.controller.auth.payload.DuplicationCheckRequest;
import com.honlife.core.app.controller.auth.payload.SignupRequest;
import com.honlife.core.app.controller.auth.payload.VerifyEmailRequest;
import com.honlife.core.app.model.mail.MailService;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.controller.auth.payload.LoginRequest;
import com.honlife.core.app.controller.auth.payload.TokenResponse;
import com.honlife.core.app.model.auth.AuthService;
import com.honlife.core.app.model.auth.dto.TokenDto;
import com.honlife.core.infra.response.CommonApiResponse;


@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final AuthService authService;
    private final MemberService memberService;
    private final MailService mailService;


    /**
     * 로그인 처리 매서드
     * @param loginRequest 로그인 요청 객체
     * @param response 응답에 사용할 서블렛 객체
     * @return AccessToken, RefreshToken 을 담아 반환
     */
    @PostMapping("/signin")
    public ResponseEntity<CommonApiResponse<TokenResponse>> login(
        @RequestBody LoginRequest loginRequest,
        HttpServletResponse response
    ) {
        TokenDto tokenDto = authService.signin(loginRequest);

        return ResponseEntity.ok(CommonApiResponse.success(TokenResponse.getTokenResponse(tokenDto, response)));
    }

    /**
     * 회원가입 요청을 처리합니다.
     *
     * @param signupRequest
     * @return 계정이 이미 존재하는 경우 {@code HttpStatus.CONFLICT}를, 그 외의 경우는 {@code HttpStatus.OK}
     */
    @PostMapping("/signup")
    public ResponseEntity<CommonApiResponse<ResponseCode>> signup(
        @RequestBody SignupRequest signupRequest
    ) {
        String userEmail = signupRequest.getEmail();
        Optional<Boolean> accountStatus = memberService.isActiveAccount(userEmail);
        // 계정 정보가 존재하는 경우
        if(accountStatus.isPresent()) {
            // 활성화된 계정인 경우
            if(accountStatus.get()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(CommonApiResponse.error(ResponseCode.CONFLICT_EXIST_MEMBER));
            }
            // 활성화되지 않은 계정인 경우, 기존 계정 업데이트
            memberService.updateNotVerifiedMember(signupRequest);
        } else {
            // 신규 회원의 경우에 새로운 정보 저장
            memberService.saveNotVerifiedMember(signupRequest);
        }
        return ResponseEntity.ok()
            .body(CommonApiResponse.success(ResponseCode.CONTINUE));
    }

    /**
     * 이메일 인증 번호 발송 처리
     * @param emailRequest 인증 검사를 진행할 이메일을 담은 요청 객체<br>
     *                     인증코드가 null 이 아닌경우 잘못된 요청으로 간주
     * @return 정상처리 된 경우 {@code HttpStatus.OK}, 정상처리가 되지 않은 경우 {@code HttpStatus.BAD_REQUEST}
     */
    @PostMapping("/auth/email")
    public ResponseEntity<CommonApiResponse<Void>> sendVerifyCode(
        @RequestBody @Valid VerifyEmailRequest emailRequest
    ){
        if(emailRequest.getCode()!=null) {
            return ResponseEntity.badRequest().body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        try {
            String userEmail = emailRequest.getEmail();
            memberService.updateMemberVerifyStatus(userEmail, false);  // 인증 상태 비활성화
            mailService.sendVerificationEmail(userEmail);
            return ResponseEntity.ok(CommonApiResponse.noContent());
        } catch (MessagingException | IOException e) {  // 메일 전송에 실패한 경우
            return ResponseEntity.internalServerError().body(CommonApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * 이메일 인증 진행
     * @param emailRequest 인증 검사를 진행할 이메일과 인증 코드를 담은 요청 객체
     * @return 인증 성공시 {@code HttpStatus.OK}를 코드가 일치하지 않으면, {@code HttpStatus.UNAUTHORIZED}를 반환합니다.
     */
    @PostMapping("/auth/code")
    public ResponseEntity<CommonApiResponse<?>> verifyEmail(
        @RequestBody @Valid VerifyEmailRequest emailRequest,
        HttpServletResponse response
    ) {
        if(emailRequest.getCode()==null) {
            return ResponseEntity.badRequest().body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        String userEmail = emailRequest.getEmail();
        if(authService.isVerifyCode(userEmail, emailRequest.getCode())) {
            memberService.updateMemberStatus(userEmail, true, true);  // 계정 활성화
            TokenDto tokenDto = authService.autoSignin(userEmail);  // 자동 로그인 처리(토큰 발급)
            return ResponseEntity.ok(CommonApiResponse.success(TokenResponse.getTokenResponse(tokenDto, response)));
        }
        return ResponseEntity.status(ResponseCode.INVALID_CODE.status())
            .body(CommonApiResponse.error(ResponseCode.INVALID_CODE));
    }

    /**
     * 중복 확인 처리 API<br>
     * 요청객체에서 이메일 또는 닉네임 중 둘 중 하나만 입력이 되어야 하며,<br>
     * 이메일, 닉네임이 모두 담겨있거나 모두 없는 경우 오류코드 반환
     * @param duplicationCheckRequest 이메일 또는 닉네임을 담는 요청 객체
     * @return 검사 성공시 {@code Boolean} 형을 응답에 담아 보내며,<br>
     * 잘못 된 요청인 경우 {@code HttpStatus.BAD_REQUEST} 반환
     */
    @PostMapping("/check")
    public ResponseEntity<CommonApiResponse<Map<String, Boolean>>> isEmailDuplicated(
        @RequestBody DuplicationCheckRequest duplicationCheckRequest
    ) {
        String email = duplicationCheckRequest.getEmail();
        String nickname = duplicationCheckRequest.getNickname();
        if(email != null && nickname == null){
            if(memberService.isEmailExists(email, true))
                return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", true)));
            return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", false)));
        } else if (email == null && nickname != null){
            if(memberService.isNicknameExists(nickname))
                return ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", true)));
            return  ResponseEntity.ok(CommonApiResponse.success(Map.of("isDuplicated", false)));
        } else {
            return ResponseEntity.badRequest().body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }
    }
}
