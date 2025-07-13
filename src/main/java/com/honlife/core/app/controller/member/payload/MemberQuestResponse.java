package com.honlife.core.app.controller.member.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberQuestResponse {

    private String questKey;

    private String questName;

    private Integer questReward;

    private String questInfo;

    private Boolean isDone;

    private Integer questProgress; // 백분율

}
