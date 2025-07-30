package com.honlife.core.app.controller.quest.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class WeeklyQuestProgressResponse {

    private Long questId;
    private Long progressId;
    private Long categoryId;
    private String questKey;
    private String questName;
    private Integer target;
    private Integer progress;
    private Integer points;
    private Boolean isRewarded;
}
