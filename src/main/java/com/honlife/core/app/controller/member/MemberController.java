package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberBadgeResponse;
import com.honlife.core.app.controller.member.payload.MemberUpdatePasswordRequest;
import com.honlife.core.app.controller.member.payload.MemberWithdrawRequest;
import com.honlife.core.app.model.badge.service.BadgeService;
import com.honlife.core.app.model.category.service.CategoryService;
import com.honlife.core.app.model.category.service.InterestCategoryService;
import com.honlife.core.app.model.member.model.MemberBadgeDTO;
import com.honlife.core.app.model.member.model.MemberBadgeDetailDTO;
import com.honlife.core.app.model.member.service.MemberBadgeService;
import com.honlife.core.app.model.member.service.MemberItemService;
import com.honlife.core.app.model.member.service.MemberPointService;
import com.honlife.core.app.model.quest.service.WeeklyQuestProgressService;
import com.honlife.core.app.model.routine.service.RoutineService;
import com.honlife.core.app.model.withdraw.code.WithdrawType;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.error.exceptions.ReferencedException;
import com.honlife.core.infra.error.exceptions.ReferencedWarning;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.controller.member.payload.MemberPayload;
import com.honlife.core.app.model.member.model.MemberDTO;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/members", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;
    private final MemberBadgeService memberBadgeService;

    /**
     * 로그인된 회원의 정보 조회
     * @param userDetails 유저 인증 정보 객체
     * @return 조회 성공시 {@code CommonApiResponse<}{@link MemberPayload}{@code >}형태로 사용자의 정보를 반한홥니다.
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<MemberResponseWrapper>> getCurrentMember(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userEmail = userDetails.getUsername();
        MemberDTO targetMember = memberService.findMemberByEmail(userEmail);

        // 해당하는 member가 없을 때
        if(targetMember == null) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
        }

        MemberPayload member = MemberPayload.fromDTO(targetMember);

        MemberBadgeDetailDTO badge = memberBadgeService.getEquippedBadge(userEmail);

        MemberResponseWrapper response = new MemberResponseWrapper(member, MemberBadgeResponse.fromDTO(badge));

        return ResponseEntity.ok(CommonApiResponse.success(response));
    }

    /**
     * 비밀번호 변경 요청 처리 API
     * @param userDetails 유저 인증 정보
     * @param updatePasswordRequest 변경할 비밀번호를 담은 객체
     * @return 변경 처리 성공시 {@code 200}을 반환합니다. <br>이메일 인증이 되어있지 않은 사용자일 경우, {@code 401}을 반환합니다. 비밀 번호 변경 중 문제가 생길 시 {@code 400}을 반환합니다.
     */
    @PatchMapping("/password")
    public ResponseEntity<CommonApiResponse<Void>> updatePassword(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody final MemberUpdatePasswordRequest updatePasswordRequest
    ) {
        String userEmail = userDetails.getUsername();

        // 이메일 인증 확인
        if(!memberService.isEmailVerified(userEmail)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonApiResponse.error(ResponseCode.BAD_CREDENTIAL));
        }

        memberService.updatePassword(userEmail, updatePasswordRequest.getNewPassword());
        return ResponseEntity.ok(CommonApiResponse.noContent());

    }

    /**
     * 회원 정보 변경 요청 처리 API
     * @param userDetails 유저 인증 정보
     * @param memberPayload 회원 정보 객체
     * @return 변경에 성공하면 {@code 200}을 반환합니다.
     * @throws org.springframework.web.bind.MethodArgumentNotValidException 클라이언트로 부터 잘못된 값이 전송된 경우
     */
    @PatchMapping
    public ResponseEntity<CommonApiResponse<Void>> updateMember(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody @Valid final MemberPayload memberPayload
    ) {
        String userEmail = userDetails.getUsername();

        memberService.updateMember(userEmail, memberPayload.toDTO());
        return ResponseEntity.ok(CommonApiResponse.noContent());

    }

    /**
     * 회원 탈퇴 요청 처리 API
     * @param userDetails 인증 정보
     * @param withdrawRequest 탈퇴 사유 타입
     * @return 탈퇴처리에 성공 시 {@code 200}을 반환합니다.
     * @throws org.springframework.web.bind.MethodArgumentNotValidException 클라이언트로 부터 잘못된 값이 전송된 경우
     */
    @DeleteMapping
    public ResponseEntity<CommonApiResponse<Void>> deleteMember(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody @Valid final MemberWithdrawRequest withdrawRequest
    ) {
        String userEmail = userDetails.getUsername();

        // 직적 입력이나 이유가 넘어오지 않을 때
        if(withdrawRequest.getWithdrawType()==WithdrawType.ETC&&withdrawRequest.getEtcReason()==null){
            return ResponseEntity.badRequest().body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }
        memberService.removeMemberReference(userEmail);

            // 제대로 처리 됐는지 검증
        final ReferencedWarning referencedWarning = memberService.getReferencedWarning(userEmail);
        if (referencedWarning != null) {
            throw new CommonException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        memberService.softDropMember(userEmail);
        // 탈퇴 사유 저장
        memberService.saveWithdrawReason(withdrawRequest);

        return ResponseEntity.ok(CommonApiResponse.noContent());

    }

    /**
     * 비밀번호 확인 요청 처리 API
     * @param userDetails 유저 인증 정보
     * @return 확인 성공시 {@code 200}을 반환합니다. 현재 비밀번호가 일치 하지 않는 경우, {@code 401}을 반환합니다.
     */
    @PostMapping("/password")
    public ResponseEntity<CommonApiResponse<Void>> checkPassword(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam String password
    ) {
        String userEmail = userDetails.getUsername();

        if(memberService.isCorrectPassword(userEmail, password)){
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonApiResponse.error(ResponseCode.BAD_CREDENTIAL));
    }

}
