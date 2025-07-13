package com.honlife.core.app.controller.admin.badge.payload;

import com.honlife.core.app.model.badge.code.BadgeTier;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminBadgeResponse {

    private Long badgeId;

    private String badgeKey;

    private String badgeName;

    private BadgeTier tier;

    private String how;

    private Integer requirement;

    private String info;

    private String categoryName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean isActive;

}
