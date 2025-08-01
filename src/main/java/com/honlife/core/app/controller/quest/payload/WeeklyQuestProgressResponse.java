package com.honlife.core.app.controller.quest.payload;

import com.honlife.core.app.model.quest.dto.MemberWeeklyQuestDTO;
import java.util.List;
import java.util.stream.Collectors;
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

    public static WeeklyQuestProgressResponse fromDTO(MemberWeeklyQuestDTO weeklyQuest) {
        return WeeklyQuestProgressResponse.builder()
            .questId(weeklyQuest.getQuestId())
            .progressId(weeklyQuest.getProgressId())
            .categoryId(weeklyQuest.getCategoryId())
            .questKey(weeklyQuest.getQuestKey())
            .questName(weeklyQuest.getQuestName())
            .target(weeklyQuest.getTarget())
            .progress(weeklyQuest.getProgress())
            .points(weeklyQuest.getPoints())
            .isRewarded(weeklyQuest.getIsDone())
            .build();
    }

    public static List<WeeklyQuestProgressResponse> fromDTOList(List<MemberWeeklyQuestDTO> weeklyQuests) {
        return weeklyQuests.stream().map(WeeklyQuestProgressResponse::fromDTO).collect(Collectors.toList());
    }
}
