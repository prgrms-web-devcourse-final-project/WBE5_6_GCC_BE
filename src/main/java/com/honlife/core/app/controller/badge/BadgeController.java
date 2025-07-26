package com.honlife.core.app.controller.badge;

import com.honlife.core.app.controller.badge.payload.BadgeEquipRequest;
import com.honlife.core.app.controller.badge.payload.BadgePageResponse;
import com.honlife.core.app.controller.badge.payload.BadgeResponse;
import com.honlife.core.app.controller.badge.payload.BadgeRewardResponse;
import com.honlife.core.app.model.badge.dto.BadgeRewardDTO;
import com.honlife.core.app.model.badge.dto.BadgeStatusDTO;
import com.honlife.core.app.model.badge.service.BadgeService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/badges", produces = MediaType.APPLICATION_JSON_VALUE)
public class BadgeController {

    private final BadgeService badgeService;

    /**
     * 업적 조회 API (페이지네이션)
     * @param page 페이지 번호 (1부터 시작)
     * @param size 페이지 크기 (기본값: 12)
     * @param userDetails 인증된 사용자 정보
     * @return 페이지네이션된 배지 정보
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<BadgePageResponse>> getAllBadges(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "12") int size,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 1. 페이지 검증
        if (page < 1) {
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }
        if (size < 1 || size > 100) {  // 최대 100개로 제한
            throw new CommonException(ResponseCode.BAD_REQUEST);
        }

        // 2. 사용자 이메일 추출
        String email = userDetails.getUsername();

        // 3. Pageable 생성 (1-based → 0-based 변환)
        Pageable pageable = PageRequest.of(page - 1, size);

        // 4. Service에서 페이지네이션된 배지 조회
        Page<BadgeStatusDTO> badgePage = badgeService.getAllBadgesWithStatus(email, pageable);

        // 5. DTO → Response 변환
        List<BadgeResponse> badgeResponses = badgePage.getContent().stream()
            .map(BadgeResponse::fromDto)
            .toList();

        // 6. PageResponse 생성
        BadgePageResponse pageResponse = BadgePageResponse.from(badgePage, badgeResponses);

        return ResponseEntity.ok(CommonApiResponse.success(pageResponse));
    }

    /**
     * 업적 보상 수령 API - 실제 구현
     * @param badgeKey 업적 key 값
     * @param userDetails 인증된 사용자 정보
     * @return BadgeRewardResponse 완료한 업적에 대한 정보 및 포인트 획득 내역
     */
    @PostMapping
    public ResponseEntity<CommonApiResponse<BadgeRewardResponse>> claimBadgeReward(
        @RequestParam String badgeKey,
        @AuthenticationPrincipal UserDetails userDetails) {

        // 1. 사용자 이메일 추출
        String email = userDetails.getUsername();

        // 2. 서비스 호출 (DTO 반환) - 예외는 GlobalExceptionHandler가 처리
        BadgeRewardDTO dto = badgeService.claimBadgeReward(badgeKey, email);

        // 3. DTO → Response 변환
        BadgeRewardResponse response = BadgeRewardResponse.builder()
            .badgeId(dto.getBadgeId())
            .badgeKey(dto.getBadgeKey())
            .badgeName(dto.getBadgeName())
            .message(dto.getMessage())
            .pointAdded(dto.getPointAdded())
            .totalPoint(dto.getTotalPoint())
            .receivedAt(dto.getReceivedAt())
            .build();

        return ResponseEntity.ok(CommonApiResponse.success(response));
    }

    /**
     * 배지 장착 상태 변경 API
     * @param badgeKey 배지 키
     * @param request 장착 상태 요청 (isEquipped: true=장착, false=해제)
     * @param userDetails 인증된 사용자 정보
     * @return 성공 응답
     */
    @PatchMapping("/{badgeKey}/equipped")
    public ResponseEntity<CommonApiResponse<Void>> updateBadgeEquipStatus(
        @PathVariable String badgeKey,
        @RequestBody @Valid BadgeEquipRequest request,
        @AuthenticationPrincipal UserDetails userDetails) {

        // 1. 사용자 이메일 추출
        String email = userDetails.getUsername();

        // 2. 서비스 호출
        badgeService.updateBadgeEquipStatus(badgeKey, email, request.getIsEquipped());

        return ResponseEntity.ok(CommonApiResponse.noContent());
    }
}