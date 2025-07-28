package com.honlife.core.app.controller.badge.payload;

import com.honlife.core.app.model.badge.code.BadgeStatus;
import com.honlife.core.app.model.badge.code.BadgeTier;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * 모든 업적을 조회할 때 반환 되는 응답 클래스.
 * 업적 정보와 사용자 보유 여부를 포함함.
 */
@Getter
@Setter
@Builder
public class BadgeResponse {

    // === 배지 기본 정보 ===
    private Long badgeId;
    private String badgeKey;
    private String badgeName;
    private BadgeTier tier;
    private String message;
    private String info;
    private String categoryName;
    private Integer requirement;        // 목표값 (항상 표시)

    // === 핵심 상태 정보 ===
    private BadgeStatus status;         // 이것만으로 UI 제어

    // === 조건부 정보 ===
    private Integer currentProgress;    // LOCKED/ACHIEVABLE일 때만 (null 가능)
    private LocalDateTime receivedDate; // OWNED/EQUIPPED일 때만 (null 가능)

    private Boolean isEquipped;

}