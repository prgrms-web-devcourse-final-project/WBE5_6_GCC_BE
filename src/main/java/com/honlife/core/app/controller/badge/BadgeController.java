package com.honlife.core.app.controller.badge;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.grepp.honlife.app.controller.badge.payload.BadgePayload;
import spring.grepp.honlife.app.controller.badge.payload.BadgeRewardPayload;
import spring.grepp.honlife.app.model.badge.code.BadgeTier;
import spring.grepp.honlife.app.model.badge.service.BadgeService;
import spring.grepp.honlife.infra.response.CommonApiResponse;
import spring.grepp.honlife.infra.response.ResponseCode;

@RequiredArgsConstructor
@Tag(name="업적", description = "업적 관련 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/badges", produces = MediaType.APPLICATION_JSON_VALUE)
public class BadgeController {

    private final BadgeService badgeService;

    /**
     * 모든 업적 조회 API
     * @return List<BadgePayload> 모든 업적에 대한 정보 + 사용자가 가지고 있는지에 대한 정보 DTO
     */
    @Operation(summary = "모든 업적 조회", description = "현재 로그인한 사용자에 대한 모든 업적의 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<BadgePayload>>> getAllBadges() {

        List<BadgePayload> achievements = new ArrayList<>();
        achievements.add(BadgePayload.builder()
            .id(1L)
            .key("clean_bronze")
            .name("초보 청소부")
            .tier(BadgeTier.BRONZE)
            .how("청소 루틴 5번 이상 성공")
            .requirement(5)
            .info("이제 청소 좀 한다고 말할 수 있겠네요!")
            .category(1L)
            .isReceived(false)
            .build());
        achievements.add(BadgePayload.builder()
            .id(2L)
            .key("cook_bronze")
            .name("초보 요리사")
            .tier(BadgeTier.BRONZE)
            .how("요리 루틴 5번 이상 성공")
            .requirement(5)
            .info("나름 계란 프라이는 할 수 있다구요!")
            .category(2L)
            .isReceived(true)
            .build());

        return ResponseEntity.ok(CommonApiResponse.success(achievements));
    }


    /**
     * 업적 보상 수령 API
     * @param key 업적 key 값
     * @return BadgeRewardPayload 완료한 업적에 대한 정보 및 포인트 획득 내역
     */
    @Operation(summary = "업적 보상 수령", description = "key 값을 통해 특정 업적의 보상을 획득합니다.")
    @PostMapping
    public ResponseEntity<CommonApiResponse<BadgeRewardPayload>> claimBadgeReward(
        @Schema(name="key", description="업적의 고유 키 값", example = "clean_bronze")
        @RequestParam String key){
        // 달성한 적 없는 업적
        if(key.equals("clean_bronze")){
            BadgeRewardPayload response =
                BadgeRewardPayload.builder()
                    .id(1L)
                    .key(key)
                    .name("초보 청소부")
                    .pointAdded(50L)
                    .totalPoint(150L)
                    .receivedAt(LocalDateTime.now())
                .build();
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }
        // 달성한 적 있는 업적
        if(key.equals("cook_bronze")){
            return ResponseEntity.status(ResponseCode.ALREADY_CLAIMED_BADGE.status())
                .body(CommonApiResponse.error(ResponseCode.ALREADY_CLAIMED_BADGE));
        }
        // 해당 하는 키가 DB에 없을 경우
        else{
            return ResponseEntity.status(ResponseCode.NOT_EXIST_BADGE.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_EXIST_BADGE));
        }
    }

}
