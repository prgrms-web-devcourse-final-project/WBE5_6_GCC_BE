package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberBadgeResponse;
import com.honlife.core.app.controller.member.payload.MemberUpdatePasswordRequest;
import com.honlife.core.app.controller.member.payload.MemberWithdrawRequest;
import com.honlife.core.app.controller.member.wrapper.MemberResponseWrapper;
import com.honlife.core.app.model.badge.code.BadgeTier;
import com.honlife.core.app.model.withdraw.code.WithdrawType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.controller.member.payload.MemberPayload;
import com.honlife.core.app.model.member.code.ResidenceExperience;
import com.honlife.core.app.model.member.model.MemberDTO;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;


@Slf4j
@Tag(name = "🔄 [회원] 회원 정보 및 관리", description = "회원관련 API 입니다.")
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
    @Operation(summary = "🔄 로그인된 회원의 정보 조회", description = "로그인된 사용자의 정보를 조회합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<CommonApiResponse<MemberResponseWrapper>> getCurrentMember(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = userDetails.getUsername();
        if(userId.equals("user01@test.com")){
            MemberPayload member = new MemberPayload();
            member.setName("홍길동");
            member.setNickname("닉네임");
            member.setResidenceExperience(ResidenceExperience.OVER_10Y);
            member.setRegionDept1("서울시");
            member.setRegionDept2("강북구");
            member.setRegionDept3("미아동");

            MemberBadgeResponse equippedBadge = MemberBadgeResponse.builder()
                .badgeKey("clean_bronze")
                .badgeName("청소 초보")
                .badgeTier(BadgeTier.BRONZE)
                .build();

            MemberResponseWrapper responseWrapper = new MemberResponseWrapper(member, equippedBadge);

            return ResponseEntity.ok(CommonApiResponse.success(responseWrapper));
        }
        return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
            .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
    }

    /**
     * 비밀번호 변경 요청 처리 API
     * @param userDetails 유저 인증 정보
     * @param updatePasswordRequest 현재 비밀번호와 변경할 비밀번호를 담은 객체
     * @return 변경 처리 성공시 {@code 200}을 반환합니다. 이메일 인증이 되지 않은 경우, {@code 401}을 반환합니다.
     */
    @Operation(summary = "✅ 비밀번호 변경", description = "사용자의 비밀번호를 변경합니다.<br>"
        + "변경할 비밀번호만 받습니다.<br>"
        + "사전에 이메일 인증이 되지 않은 회원의 경우, 401응답이 반환됩니다.")
    @PatchMapping("/password")
    public ResponseEntity<CommonApiResponse<Void>> updatePassword(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody final MemberUpdatePasswordRequest updatePasswordRequest
    ) {
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 회원 정보 변경 요청 처리 API
     * @param userDetails 유저 인증 정보
     * @param memberPayload 회원 정보 객체
     * @return 변경에 성공하면 {@code 200}을 반환합니다.
     * @throws org.springframework.web.bind.MethodArgumentNotValidException 클라이언트로 부터 잘못된 값이 전송된 경우
     */
    @Operation(summary="✅ 회원정보 업데이트", description="회원정보를 업데이트 합니다.<br>"
        + "이름, 닉네임은 필수 정보입니다. 나머지 정보는 비어있어도 되지만, 요청에는 포함되어있어야 합니다.")
    @PatchMapping
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
    @Operation(summary = "✅ 회원 탈퇴", description = "회원탈퇴를 처리합니다.<br>"
        + "withdrawType은 비어있어서는 안되며, '기타'타입에 해당되어 사용자의 직접적인 의견을 받은 경우, etcReason에 해당 내용을 담아주세요.<br>"
        + "~ WithdrawType ~<br>"
        + "TOO_MUCH_EFFORT<br>"
        + "ROUTINE_MISMATCH<br>"
        + "UX_ISSUE<br>"
        + "MISSING_FEATURE<br>"
        + "USING_OTHER_APP<br>"
        + "NO_MOTIVATION<br>"
        + "ETC<br>")
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
            if(withdrawRequest.getWithdrawType()== WithdrawType.ETC && withdrawRequest.getEtcReason().isBlank())
                return ResponseEntity.badRequest().body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        return ResponseEntity.internalServerError().body(CommonApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR));
    }

    /**
     * 비밀번호 확인 요청 처리 API
     * @param userDetails 유저 인증 정보
     * @return 확인 성공시 {@code 200}을 반환합니다. 현재 비밀번호가 일치 하지 않는 경우, {@code 401}을 반환합니다.
     */
    @Operation(summary = "✅ 비밀번호 확인", description = "사용자의 비밀번호가 맞는지 확인합니다.<br>"
        + "현재 비밀번호를 받으며, 일치하지 않을 경우 401 응답이 반환됩니다.<br>")
    @PostMapping("/password")
    public ResponseEntity<CommonApiResponse<Void>> checkPassword(
        @AuthenticationPrincipal UserDetails userDetails,
        @Schema(description = "입력된 비밀번호", example = "1111")
        @RequestParam String password
    ) {
        String userEmail = userDetails.getUsername();

        //TODO: Dev때 Service로 옮기기
        if(userEmail.equals("user01@test.com") && passwordEncoder.matches(password, passwordEncoder.encode("1111"))){
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonApiResponse.error(ResponseCode.BAD_CREDENTIAL));
    }

}
