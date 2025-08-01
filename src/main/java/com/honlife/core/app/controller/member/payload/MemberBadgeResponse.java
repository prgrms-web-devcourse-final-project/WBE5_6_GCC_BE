package com.honlife.core.app.controller.member.payload;

import com.honlife.core.app.model.badge.code.BadgeTier;
import com.honlife.core.app.model.member.model.MemberBadgeDetailDTO;
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

    public static MemberBadgeResponse fromDTO(MemberBadgeDetailDTO detailDTO) {
        if(detailDTO == null){
            return null;
        }

        return MemberBadgeResponse.builder()
            .badgeKey(detailDTO.getBadgeKey())
            .badgeName(detailDTO.getBadgeName())
            .badgeTier(detailDTO.getBadgeTier())
            .build();
    }
}
