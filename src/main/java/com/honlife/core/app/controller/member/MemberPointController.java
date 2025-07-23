package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberPointResponse;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.member.service.MemberPointService;

@Tag(name="✅ [회원] 포인트", description = "회원이 보유한 포인트 관련 API들")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(value = "/api/v1/members/points", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberPointController {

    private final MemberPointService memberPointService;

    public MemberPointController(final MemberPointService memberPointService) {
        this.memberPointService = memberPointService;
    }

    /**
     * 현재 로그인한 멤버의 보유 포인트량을 조회하는 API
     * @return MemberPointPayload 멤버의 아이디와 현재 보유한 포인트를 담음
     */
    @GetMapping
    @Operation(summary = "로그인된 회원의 보유 포인트 조회", description = "로그인된 사용자의 보유 포인트를 조회합니다.")
    public ResponseEntity<CommonApiResponse<MemberPointResponse>> getMemberPoint(
        @AuthenticationPrincipal UserDetails userDetails) {
        // 유저 데이터를 가져와 nickname과 포인트 조회
        MemberPointResponse memberPointPayload
            = MemberPointResponse.builder()
            .points(500)
            .build();

        return ResponseEntity.ok(CommonApiResponse.success(memberPointPayload));
    }
}
