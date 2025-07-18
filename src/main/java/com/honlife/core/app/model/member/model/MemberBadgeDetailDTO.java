package com.honlife.core.app.model.member.model;

import com.honlife.core.app.model.badge.code.BadgeTier;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원이 보유한 배지의 상세 정보를 담는 DTO
 * MemberBadge 정보 + Badge 정보를 모두 포함
 */
@Getter
@Setter
@Builder
public class MemberBadgeDetailDTO {

    // MemberBadge 정보
    private Long memberBadgeId;
    private LocalDateTime receivedDate;  // 배지 획득 일시

    // Badge 정보
    private Long badgeId;
    private String badgeKey;
    private String badgeName;
    private BadgeTier badgeTier;
    private String badgeInfo;
}
