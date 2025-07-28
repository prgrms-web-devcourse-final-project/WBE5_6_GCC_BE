package com.honlife.core.app.model.badge.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 배지 보상 수령 시 반환되는 DTO
 * 배지 정보와 포인트 획득 내역을 포함
 */
@Getter
@Setter
@Builder
public class BadgeRewardDTO {

    /**
     * 획득한 배지 ID
     */
    private Long badgeId;

    /**
     * 획득한 배지 키
     */
    private String badgeKey;

    /**
     * 획득한 배지명
     */
    private String badgeName;

    /**
     * 배지 획득 시 축하 메시지
     */
    private String message;

    /**
     * 이번에 획득한 포인트
     */
    private Long pointAdded;

    /**
     * 획득 후 총 포인트
     */
    private Long totalPoint;

    /**
     * 배지 획득 일시
     */
    private LocalDateTime receivedAt;
}
