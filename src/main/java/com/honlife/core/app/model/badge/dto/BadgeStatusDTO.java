package com.honlife.core.app.model.badge.dto;

import com.honlife.core.app.model.badge.code.BadgeStatus;
import com.honlife.core.app.model.badge.code.BadgeTier;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BadgeStatusDTO {

    /**
     * 배지 기본 정보
     */
    private Long badgeId;
    private String badgeKey;
    private String badgeName;
    private BadgeTier tier;
    private String message;
    private String info;        // 달성 조건 설명
    private Integer requirement; // 목표값
    private String categoryName;

    /**
     * 사용자별 상태 정보
     */
    private BadgeStatus status;  // LOCKED/ACHIEVABLE/OWNED/EQUIPPED

    /**
     * 진행률 정보 (LOCKED/ACHIEVABLE 상태일 때 사용)
     */
    private Integer currentProgress; // 현재 진행도

    /**
     * 획득/장착 정보
     */
    private LocalDateTime receivedDate;  // 획득일시 (미획득시 null)
    private Boolean isEquipped;          // 현재 장착 여부

}
