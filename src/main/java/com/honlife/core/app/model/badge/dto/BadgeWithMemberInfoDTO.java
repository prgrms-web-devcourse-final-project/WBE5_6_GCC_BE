package com.honlife.core.app.model.badge.dto;

import com.honlife.core.app.model.badge.code.BadgeTier;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BadgeWithMemberInfoDTO {

    /**
     * 배지 기본 정보
     */
    private Long badgeId;
    private String badgeKey;
    private String badgeName;
    private BadgeTier tier;
    private String message;
    private Integer requirement;
    private String info;

    /**
     * 카테고리명 (조인으로 가져온 값)
     */
    private String categoryName;

    /**
     * 현재 사용자가 이 배지를 획득했는지 여부
     */
    private Boolean isReceived;

    /**
     * 배지를 획득한 일시 (획득하지 않았다면 null)
     */
    private LocalDateTime receivedDate;

}
