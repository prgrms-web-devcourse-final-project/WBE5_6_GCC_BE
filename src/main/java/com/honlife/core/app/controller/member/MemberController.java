package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberUpdatePasswordRequest;
import com.honlife.core.app.controller.member.payload.MemberWithdrawRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.controller.member.payload.MemberPayload;
import com.honlife.core.app.model.member.code.ResidenceExperience;
import com.honlife.core.app.model.member.model.MemberDTO;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;


@Slf4j
@Tag(name = "회원", description = "회원관련 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/members", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearerAuth")
public class MemberController {

    private final MemberService memberService;
    //TODO: Dev로 넘어가면 Service로 넘기기
    private final PasswordEncoder passwordEncoder;

    public MemberController(final MemberService memberService, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        //TODO: Dev로 넘어가면 Service로 넘기기
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 로그인된 회원의 정보 조회
     * @param userDetails 유저 인증 정보 객체
     * @return 조회 성공시 {@code CommonApiResponse<}{@link MemberPayload}{@code >}형태로 사용자의 정보를 반한홥니다.
     */
    @Operation(summary = "로그인된 회원의 정보 조회", description = "로그인된 사용자의 정보를 조회합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<CommonApiResponse<MemberPayload>> getCurrentMember(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = userDetails.getUsername();
        if(userId.equals("user01@test.com")){
            MemberPayload response = new MemberPayload();
            response.setName("홍길동");
            response.setNickname("닉네임");
            response.setResidenceExperience(ResidenceExperience.OVER_10Y);
            response.setRegionDept1("서울시");
            response.setRegionDept2("강북구");
            response.setRegionDept3("미아동");
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }
        return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
            .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
    }

    /**
     * 비밀번호 변경 요청 처리 API
     * @param userDetails 유저 인증 정보
     * @param updatePasswordRequest 현재 비밀번호와 변경할 비밀번호를 담은 객체
     * @return 변경 처리 성공시 {@code 200}을 반환합니다. 현재 비밀번호가 일치 하지 않는 경우, {@code 401}을 반환합니다.
     */
    @Operation(summary = "비밀번호 변경", description = "사용자의 비밀번호를 변경합니다.<br>"
        + "현재 비밀번호와 변경할 비밀번호를 받으며, 내부적으로 비밀번호 비교 후 비밀번호가 일치할 때 변경합니다.<br>"
        + "비밀번호가 일치하지 않는 경우, 401응답이 반환됩니다.")
    @PutMapping("/password")
    public ResponseEntity<CommonApiResponse<Void>> updatePassword(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody final MemberUpdatePasswordRequest updatePasswordRequest
    ) {
        String userEmail = userDetails.getUsername();
        String userPassword = userDetails.getPassword();
        //TODO: Dev때 Service로 옮기기
        if(userEmail.equals("user01@test.com") && passwordEncoder.matches("1111", userPassword)){
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonApiResponse.error(ResponseCode.BAD_CREDENTIAL));
    }

    /**
     * 회원 정보 변경 요청 처리 API
     * @param userDetails 유저 인증 정보
     * @param memberPayload 회원 정보 객체
     * @return 변경에 성공하면 {@code 200}을 반환합니다.
     * @throws org.springframework.web.bind.MethodArgumentNotValidException 클라이언트로 부터 잘못된 값이 전송된 경우
     */
    @Operation(summary="회원정보 업데이트", description="회원정보를 업데이트 합니다.<br>"
        + "이름, 닉네임은 필수 정보입니다. 나머지 정보는 비어있어도 되지만, 요청에는 포함되어있어야 합니다.")
    @PutMapping
    public ResponseEntity<CommonApiResponse<Void>> updateMember(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody @Valid final MemberPayload memberPayload
    ) {
        String userEmail = userDetails.getUsername();
        if(userEmail.equals("user01@test.com")){
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        return ResponseEntity.internalServerError().body(CommonApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR));
    }

    /**
     * 회원 탈퇴 요청 처리 API
     * @param userDetails 인증 정보
     * @param withdrawRequest 탈퇴 사유 타입
     * @return 탈퇴처리에 성공 시 {@code 200}을 반환합니다.
     * @throws org.springframework.web.bind.MethodArgumentNotValidException 클라이언트로 부터 잘못된 값이 전송된 경우
     */
    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "회원탈퇴를 처리합니다.<br>"
        + "withdrawType은 비어있어서는 안되며, '기타'타입에 해당되어 사용자의 직접적인 의견을 받은 경우, etcReason에 해당 내용을 담아주세요.")
    public ResponseEntity<CommonApiResponse<Void>> deleteMember(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody @Valid final MemberWithdrawRequest withdrawRequest
    ) {
//        final ReferencedWarning referencedWarning = memberService.getReferencedWarning(id);
//        if (referencedWarning != null) {
//            throw new ReferencedException(referencedWarning);
//        }
//        memberService.delete(id);

        // 예시 응답
        String userEmail = userDetails.getUsername();
        if(userEmail.equals("user01@test.com")) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        return ResponseEntity.internalServerError().body(CommonApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR));
    }

}
