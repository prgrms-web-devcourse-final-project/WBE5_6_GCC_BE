package com.honlife.core.app.controller.badge.payload;

import com.honlife.core.app.model.badge.code.BadgeTier;
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

    private Long badgeId;

    private String badgeKey;

    private String badgeName;

    private BadgeTier tier;

    private String how;

    private Integer requirement;

    private String info;

    private String category;

    private Boolean isReceived;


}
