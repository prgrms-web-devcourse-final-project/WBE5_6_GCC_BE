package com.honlife.core.app.controller.badge;

import com.honlife.core.app.controller.badge.payload.BadgePageResponse;
import com.honlife.core.app.controller.badge.payload.BadgeResponse;
import com.honlife.core.app.controller.badge.payload.BadgeRewardResponse;
import com.honlife.core.app.model.badge.code.BadgeStatus;
import com.honlife.core.app.model.badge.code.BadgeTier;
import com.honlife.core.infra.payload.PageParam;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name="✅ [회원] 업적", description = "업적 관련 API 입니다.")
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(value = "/api/v1/badges", produces = MediaType.APPLICATION_JSON_VALUE)
public class BadgeController {

    /**
     * 업적 조회 API (페이지네이션)
     * @param pageParam 페이지 번호/크기
     * @param userDetails 인증된 사용자 정보
     * @return 페이지네이션된 배지 정보
     */
    @Operation(summary = "✅ 업적 조회 (페이지네이션)",
        description = "모든 업적의 정보를 페이지네이션으로 조회합니다. " +
            "각각의 배지 상태(LOCKED/ACHIEVABLE/OWNED/EQUIPPED)와 진행률을 확인할 수 있습니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<BadgePageResponse>> getAllBadges(
        @Valid PageParam pageParam,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<BadgeResponse> responses = new ArrayList<>();

        // 실제 테스트 데이터 기반 모킹 데이터 - 다양한 상태의 배지들

        // 청소/정리 카테고리 (일부만 표시)
        responses.add(BadgeResponse.builder()
            .badgeId(1L)
            .badgeKey("CLEAN_BRONZE")
            .badgeName("청소/정리 초보")
            .tier(BadgeTier.BRONZE)
            .message("청소/정리를 시작했어요!")
            .info("청소/정리 루틴 5회 달성")
            .requirement(5)
            .categoryName("청소/정리")
            .status(BadgeStatus.LOCKED)
            .currentProgress(2)  // 2/5 진행 중
            .receivedDate(null)
            .isEquipped(false)
            .build());

        responses.add(BadgeResponse.builder()
            .badgeId(2L)
            .badgeKey("CLEAN_SILVER")
            .badgeName("청소/정리 중급")
            .tier(BadgeTier.SILVER)
            .message("이제 조금 익숙해졌어요!")
            .info("청소/정리 루틴 10회 달성")
            .requirement(10)
            .categoryName("청소/정리")
            .status(BadgeStatus.LOCKED)
            .currentProgress(0)
            .receivedDate(null)
            .isEquipped(false)
            .build());

        // 요리 카테고리
        responses.add(BadgeResponse.builder()
            .badgeId(13L)
            .badgeKey("COOK_BRONZE")
            .badgeName("요리 초보")
            .tier(BadgeTier.BRONZE)
            .message("요리를 시작했어요!")
            .info("요리 루틴 5회 달성")
            .requirement(5)
            .categoryName("요리")
            .status(BadgeStatus.ACHIEVABLE)
            .currentProgress(5)  // 달성 완료, 수령 대기
            .receivedDate(null)
            .isEquipped(false)
            .build());

        responses.add(BadgeResponse.builder()
            .badgeId(14L)
            .badgeKey("COOK_SILVER")
            .badgeName("요리 중급")
            .tier(BadgeTier.SILVER)
            .message("요리에 익숙해졌어요!")
            .info("요리 루틴 10회 달성")
            .requirement(10)
            .categoryName("요리")
            .status(BadgeStatus.LOCKED)
            .currentProgress(0)
            .receivedDate(null)
            .isEquipped(false)
            .build());

        // 건강 카테고리
        responses.add(BadgeResponse.builder()
            .badgeId(25L)
            .badgeKey("HEALTH_BRONZE")
            .badgeName("건강 초보")
            .tier(BadgeTier.BRONZE)
            .message("건강을 챙기기 시작했어요!")
            .info("건강 루틴 5회 달성")
            .requirement(5)
            .categoryName("건강")
            .status(BadgeStatus.OWNED)
            .currentProgress(null)  // 이미 획득했으므로 진행률 표시 안함
            .receivedDate(LocalDateTime.now().minusDays(3))
            .isEquipped(false)
            .build());

        responses.add(BadgeResponse.builder()
            .badgeId(26L)
            .badgeKey("HEALTH_SILVER")
            .badgeName("건강 중급")
            .tier(BadgeTier.SILVER)
            .message("몸이 가벼워졌어요!")
            .info("건강 루틴 10회 달성")
            .requirement(10)
            .categoryName("건강")
            .status(BadgeStatus.LOCKED)
            .currentProgress(0)
            .receivedDate(null)
            .isEquipped(false)
            .build());

        // 출석 배지 (카테고리 없음)
        responses.add(BadgeResponse.builder()
            .badgeId(33L)
            .badgeKey("STREAK_BRONZE")
            .badgeName("꾸준함의 시작")
            .tier(BadgeTier.BRONZE)
            .message("3일 연속 접속! 시작이 반이죠!")
            .info("연속 접속 3일 달성")
            .requirement(3)
            .categoryName(null)  // 출석 배지는 카테고리 없음
            .status(BadgeStatus.EQUIPPED)
            .currentProgress(null)
            .receivedDate(LocalDateTime.now().minusDays(1))
            .isEquipped(true)
            .build());

        responses.add(BadgeResponse.builder()
            .badgeId(34L)
            .badgeKey("STREAK_SILVER")
            .badgeName("루틴 러너")
            .tier(BadgeTier.SILVER)
            .message("일주일 동안 빠짐없이 접속했어요!")
            .info("연속 접속 7일 달성")
            .requirement(7)
            .categoryName(null)
            .status(BadgeStatus.LOCKED)
            .currentProgress(3)  // 3/7 진행 중
            .receivedDate(null)
            .isEquipped(false)
            .build());

        // 세탁/의류 카테고리
        responses.add(BadgeResponse.builder()
            .badgeId(5L)
            .badgeKey("LAUNDRY_BRONZE")
            .badgeName("세탁/의류 초보")
            .tier(BadgeTier.BRONZE)
            .message("빨래를 시작했어요!")
            .info("세탁/의류 루틴 5회 달성")
            .requirement(5)
            .categoryName("세탁/의류")
            .status(BadgeStatus.LOCKED)
            .currentProgress(1)
            .receivedDate(null)
            .isEquipped(false)
            .build());

        // 페이지네이션 모킹 (간단하게 처리)
        int start = (pageParam.getPage() - 1) * pageParam.getSize();
        int end = Math.min(start + pageParam.getSize(), responses.size());
        List<BadgeResponse> pageContent = responses.subList(start, end);

        BadgePageResponse pageResponse = BadgePageResponse.builder()
            .content(pageContent)
            .totalElements((long) responses.size())
            .totalPages((int) Math.ceil((double) responses.size() / pageParam.getSize()))
            .currentPage(pageParam.getPage())
            .size(pageParam.getSize())
            .hasNext(end < responses.size())
            .hasPrevious(pageParam.getPage() > 1)
            .first(pageParam.getPage() == 1)
            .last(end >= responses.size())
            .build();

        return ResponseEntity.ok(CommonApiResponse.success(pageResponse));
    }



    /**
     * 업적 보상 수령 API
     * @param badgeKey 업적 key 값
     * @param userDetails 인증된 사용자 정보
     * @return BadgeRewardResponse 완료한 업적에 대한 정보 및 포인트 획득 내역
     */
    @Operation(summary = "✅ 업적 보상 수령",
        description = "badge_key 값을 통해 특정 업적의 보상을 획득합니다. " +
            "<br> COOK_BRONZE 입력 시 200 코드가 반환됩니다. (포인트: 100)" +
            "<br> CLEAN_BRONZE 입력 시 달성 조건 미충족 에러가 반환됩니다." +
            "<br> HEALTH_BRONZE 입력 시 중복 수령 에러가 반환됩니다.")
    @PostMapping
    public ResponseEntity<CommonApiResponse<BadgeRewardResponse>> claimBadgeReward(
        @Schema(description="업적의 고유 키 값", example = "COOK_BRONZE")
        @RequestParam String badgeKey,
        @AuthenticationPrincipal UserDetails userDetails) {

        // 달성 완료 → 수령 가능
        if(badgeKey.equals("COOK_BRONZE")){
            BadgeRewardResponse response = BadgeRewardResponse.builder()
                .badgeId(13L)
                .badgeKey(badgeKey)
                .badgeName("요리 초보")
                .message("요리를 시작했어요!")
                .pointAdded(100L)  // 테스트 데이터 기준
                .totalPoint(250L)
                .receivedAt(LocalDateTime.now())
                .build();
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }

        // 달성 조건 미충족
        if(badgeKey.equals("CLEAN_BRONZE")){
            return ResponseEntity.status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        // 이미 수령한 배지
        if(badgeKey.equals("HEALTH_BRONZE")){
            return ResponseEntity.status(ResponseCode.GRANT_CONFLICT_BADGE.status())
                .body(CommonApiResponse.error(ResponseCode.GRANT_CONFLICT_BADGE));
        }

        // 존재하지 않는 배지
        return ResponseEntity.status(ResponseCode.NOT_FOUND_BADGE.status())
            .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_BADGE));
    }

    /**
     * 배지 장착/해제 토글 API
     * @param badgeKey 배지 키
     * @param userDetails 인증된 사용자 정보
     * @return 성공 응답
     */
    @Operation(summary = "✅ 배지 장착/해제",
        description = "보유한 배지의 장착 상태를 토글합니다. " +
            "<br> HEALTH_BRONZE: 미장착 → 장착" +
            "<br> STREAK_BRONZE: 장착 중 → 해제" +
            "<br> COOK_BRONZE: 미보유 배지 → 에러")
    @PatchMapping
    public ResponseEntity<CommonApiResponse<Void>> updateBadgeEquipStatus(
        @Schema(description="배지의 고유 키 값", example = "HEALTH_BRONZE")
        @RequestParam String badgeKey,
        @AuthenticationPrincipal UserDetails userDetails) {

        // 보유한 배지들만 장착 가능
        if(badgeKey.equals("HEALTH_BRONZE") || badgeKey.equals("STREAK_BRONZE")){
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }

        // 미보유 배지
        return ResponseEntity.status(ResponseCode.NOT_FOUND_BADGE.status())
            .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_BADGE));
    }
}