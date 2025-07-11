package com.honlife.core.app.controller.member.payload;

import com.honlife.core.app.model.badge.code.BadgeTier;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberBadgeResponse {

    private String badgeKey;

    private String badgeName;

    private BadgeTier badgeTier;

    private String how;

    private Integer requirement;

    private String info;

    private String categoryName;

}
