package com.honlife.core.app.controller.badge;

import com.honlife.core.app.controller.badge.payload.BadgeResponse;
import com.honlife.core.app.controller.badge.payload.BadgeRewardResponse;
import com.honlife.core.app.model.badge.dto.BadgeWithMemberInfoDTO;
import com.honlife.core.app.model.badge.service.BadgeService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/api/v1/badges", produces = MediaType.APPLICATION_JSON_VALUE)
public class BadgeController {

    private final BadgeService badgeService;

    /**
     * 업적 조회 API
     * @return List<BadgePayload> 모든 업적에 대한 정보
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<BadgeResponse>>> getAllBadges(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 1. 사용자 이메일 추출
        String email = userDetails.getUsername();

        // 2. Service에서 모든 배지 정보 조회
        List<BadgeWithMemberInfoDTO> dtos = badgeService.getAllBadgesWithMemberInfo(email);

        // 3. DTO → Response 변환
        List<BadgeResponse> responses = dtos.stream()
            .map(BadgeResponse::fromDto)
            .toList();

        return ResponseEntity.ok(CommonApiResponse.success(responses));
    }

    /**
     * 업적 단건 조회 API
     * @return BadgePayload 특정 업적에 대한 정보
     */
    @GetMapping("/{key}")
    public ResponseEntity<CommonApiResponse<BadgeResponse>> getBadge(
        @Schema(name="key", description="업적의 고유 키 값", example = "clean_bronze")
        @PathVariable(name="key") String badgeKey,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 1. 사용자 이메일 추출
        String email = userDetails.getUsername();

        // 2. Service에서 배지 정보 조회
        BadgeWithMemberInfoDTO dto = badgeService.getBadgeWithMemberInfo(badgeKey, email);

        // 3. Badge가 없을 때 에러 처리
        if (dto == null) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_BADGE.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_BADGE));
        }

        // 4. DTO → Response 변환
        BadgeResponse response = BadgeResponse.fromDto(dto);

        return ResponseEntity.ok(CommonApiResponse.success(response));
    }

    /**
     * 업적 보상 수령 API
     * @param badgeKey 업적 key 값
     * @return BadgeRewardPayload 완료한 업적에 대한 정보 및 포인트 획득 내역
     */
    @Operation(summary = "업적 보상 수령", description = "badge_key 값을 통해 특정 업적의 보상을 획득합니다.")
    @PostMapping
    public ResponseEntity<CommonApiResponse<BadgeRewardResponse>> claimBadgeReward(
        @Schema(name="key", description="업적의 고유 키 값", example = "clean_bronze")
        @RequestParam String badgeKey){
        // 달성한 적 없는 업적
        if(badgeKey.equals("clean_bronze")){
            BadgeRewardResponse response =
                BadgeRewardResponse.builder()
                    .badgeId(1L)
                    .badgeKey(badgeKey)
                    .badgeName("초보 청소부")
                    .pointAdded(50L)
                    .totalPoint(150L)
                    .receivedAt(LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }
        // 달성한 적 있는 업적
        if(badgeKey.equals("cook_bronze")){
            return ResponseEntity.status(ResponseCode.GRANT_CONFLICT_BADGE.status())
                .body(CommonApiResponse.error(ResponseCode.GRANT_CONFLICT_BADGE));
        }
        // 해당 하는 키가 DB에 없을 경우
        else{
            return ResponseEntity.status(ResponseCode.NOT_FOUND_BADGE.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_BADGE));
        }
    }
}