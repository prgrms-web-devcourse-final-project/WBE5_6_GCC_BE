package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberUpdatePasswordRequest;
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

    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.findAll());
    }

    /**
     * 로그인된 회원의 정보 조회
     * @param userDetails
     * @return
     */
    @Operation(summary = "로그인된 회원의 정보 조회", description = "로그인된 사용자의 정보를 조회합니다.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/me")
    public ResponseEntity<CommonApiResponse<MemberPayload>> getCurrentMember(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = userDetails.getUsername();
        if(userId.equals("user01@test.com")){
            MemberPayload response = new MemberPayload();
            response.setEmail("user01@test.com");
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

    @PostMapping("/update/password")
    public ResponseEntity<CommonApiResponse<Void>> updatePassword(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody MemberUpdatePasswordRequest updatePasswordRequest
    ) {
        String userEmail = userDetails.getUsername();
        String userPassword = userDetails.getPassword();
        //TODO: Dev때 Service로 옮기기
        if(userEmail.equals("user01@test.com") && passwordEncoder.matches("1111", userPassword)){
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CommonApiResponse.error(ResponseCode.BAD_CREDENTIAL));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateMember(@PathVariable(name = "id") final Long id,
        @RequestBody @Valid final MemberDTO memberDTO) {
        memberService.update(id, memberDTO);
        return ResponseEntity.ok(id);
    }

    /**
     * 회원 삭제 (아직 미구현 상태입니다.)
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "특정 회원 삭제", description = "특정 회원에 대한 정보를 삭제합니다.")
    public ResponseEntity<CommonApiResponse<Void>> deleteMember(
        @PathVariable(name = "id")
        @Schema(description = "사용자 식별 id", example = "10000") final Long id
    ) {
//        final ReferencedWarning referencedWarning = memberService.getReferencedWarning(id);
//        if (referencedWarning != null) {
//            throw new ReferencedException(referencedWarning);
//        }
//        memberService.delete(id);

        // 예시 응답
        if(id == 10000) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        } else {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
        }

    }

}
