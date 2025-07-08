package com.honlife.core.app.controller.badge;

import com.honlife.core.app.model.badge.code.BadgeTier;
import com.honlife.core.app.model.badge.service.BadgeService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import com.honlife.core.app.controller.badge.payload.BadgePayload;
import com.honlife.core.app.controller.badge.payload.BadgeRewardPayload;


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
    @ApiResponse(
        responseCode = "2000",
        description = "OK",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            examples = @ExampleObject(
                name="업적 정보 예시",
                value = """
                    {
                    "status": 2000,
                    "message" : "OK",
                    "data":{
                        "achievements": [
                            {
                                "id": 1,
                                "key": "clean_bronze",
                                "name": "초보 청소부",
                                "tier": "BRONZE",
                                "how": "청소 루틴 5번 이상 성공",
                                "requirement": 5,
                                "info": "이제 청소 좀 한다고 말할 수 있겠네요!",
                                "category": "청소",
                                "isReceived": false
                            },
                            {
                              "id": 2,
                              "key": "cook_bronze",
                              "name": "초보 요리사",
                              "tier": "BRONZE",
                              "how": "요리 루틴 5번 이상 성공",
                              "requirement": 5,
                              "info": "나름 계란 프라이는 할 수 있다구요!",
                              "category": "요리",
                              "isReceived": true
                            }
                        ]
                    }
                    }
                    """
            )
        )
    )
    @Operation(summary = "모든 업적 조회", description = "현재 로그인한 사용자에 대한 모든 업적의 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<BadgePayload>>> getAllBadges() {

        List<BadgePayload> achievements = new ArrayList<>();
        achievements.add(BadgePayload.builder()
            .badgeId(1L)
            .badgeKey("clean_bronze")
            .badgeName("초보 청소부")
            .tier(BadgeTier.BRONZE)
            .how("청소 루틴 5번 이상 성공")
            .requirement(5)
            .info("이제 청소 좀 한다고 말할 수 있겠네요!")
            .category("청소")
            .isReceived(false)
            .build());
        achievements.add(BadgePayload.builder()
            .badgeId(2L)
            .badgeKey("cook_bronze")
            .badgeName("초보 요리사")
            .tier(BadgeTier.BRONZE)
            .how("요리 루틴 5번 이상 성공")
            .requirement(5)
            .info("나름 계란 프라이는 할 수 있다구요!")
            .category("요리")
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
                    .badgeId(1L)
                    .badgeKey(key)
                    .badgeName("초보 청소부")
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
