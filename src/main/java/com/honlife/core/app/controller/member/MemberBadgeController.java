package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberBadgeResponse;
import com.honlife.core.app.model.badge.code.BadgeTier;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.member.service.MemberBadgeService;

@Tag(name="회원 보유 업적", description = "현재 로그인한 회원이 보유하고 있는 업적 관련 API 입니다.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(value = "/api/v1/members/badges", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberBadgeController {

    private final MemberBadgeService memberBadgeService;

    public MemberBadgeController(final MemberBadgeService memberBadgeService) {
        this.memberBadgeService = memberBadgeService;
    }

    /**
     * 현재 로그인한 회원이 보유하는 모든 업적을 조회하는 API
     * @return
     */
    @Operation(summary = "로그인 한 회원이 보유한 업적 조회", description = "로그인된 사용자가 보유한 업적를 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<MemberBadgeResponse>>> getAllMemberBadges(@AuthenticationPrincipal UserDetails userDetails) {
        // 유저 데이터를 가져와 보유한 뱃지 조회
        List<MemberBadgeResponse> responses = new ArrayList<>();
        responses.add(MemberBadgeResponse.builder()
                .badgeKey("cook_bronze")
                .badgeName("초보 요리사")
                .badgeTier(BadgeTier.BRONZE)
                .how("요리 루틴 5번 이상 성공")
                .requirement(5)
                .info("나름 계란 프라이는 할 수 있다구요!")
                .categoryName("요리")
            .build());

        return ResponseEntity.ok(CommonApiResponse.success(responses));
    }
}
