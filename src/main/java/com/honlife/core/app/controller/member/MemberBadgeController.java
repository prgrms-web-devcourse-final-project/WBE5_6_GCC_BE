package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberBadgeResponse;
import com.honlife.core.app.model.badge.code.BadgeTier;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.member.service.MemberBadgeService;

@Tag(name="🔄 [회원] 업적", description = "현재 로그인한 회원이 보유하고 있는 업적 관련 API 입니다.")
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
    @Operation(summary = "✅ 로그인 한 회원이 보유한 업적 조회", description = "로그인된 사용자가 보유한 업적를 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<MemberBadgeResponse>>> getAllMemberBadges(@AuthenticationPrincipal UserDetails userDetails) {
        // 유저 데이터를 가져와 보유한 뱃지 조회
        List<MemberBadgeResponse> responses = new ArrayList<>();
        responses.add(MemberBadgeResponse.builder()
                .badgeKey("cook_bronze")
                .badgeName("초보 요리사")
                .badgeTier(BadgeTier.BRONZE)
            .build());

        return ResponseEntity.ok(CommonApiResponse.success(responses));
    }

    @Operation(summary = "❌ 업적 장착", description = "업적의 칭호를 장착합니다.")
    @PostMapping("/{key}")
    public ResponseEntity<CommonApiResponse<Void>> equipBadge (
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable(name = "key") @Schema(description = "장착할 업적의 키 값", example = "cook_bronze") final String badgeKey
    ) {
        return ResponseEntity.ok().body(CommonApiResponse.noContent());
    }
}
