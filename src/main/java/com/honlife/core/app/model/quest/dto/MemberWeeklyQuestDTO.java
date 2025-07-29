package com.honlife.core.app.model.quest.dto;

import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.quest.domain.WeeklyQuestProgress;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberWeeklyQuestDTO {

    private Long questId;
    private Long progressId;
    private Long categoryId;
    private String questKey;
    private String questName;
    private Integer target;
    private Integer progress;
    private Integer points;

    /**
     * 엔티티를 DTO로 변환<br>
     * category_id는 nullable이므로, category가 null인경우 그대로 null 반환
     * @param weeklyQuestProgress 주간 퀘스트 진행도 엔티티
     * @return {@code MemberWeeklyQuestDTO}
     */
    public static MemberWeeklyQuestDTO fromEntity(WeeklyQuestProgress weeklyQuestProgress, Integer points) {
        Category category = weeklyQuestProgress.getWeeklyQuest().getCategory();
        return MemberWeeklyQuestDTO.builder()
            .questId(weeklyQuestProgress.getWeeklyQuest().getId())
            .progressId(weeklyQuestProgress.getId())
            .categoryId(category != null ? category.getId() : null)
            .questKey(weeklyQuestProgress.getWeeklyQuest().getKey())
            .questName(weeklyQuestProgress.getWeeklyQuest().getName())
            .target(weeklyQuestProgress.getWeeklyQuest().getTarget())
            .progress(weeklyQuestProgress.getProgress())
            .points(points)
            .build();
    }
}
