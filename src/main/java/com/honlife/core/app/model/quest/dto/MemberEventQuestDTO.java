package com.honlife.core.app.model.quest.dto;

import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.quest.domain.EventQuestProgress;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberEventQuestDTO {

    private Long questId;
    private Long progressId;
    private Long categoryId;
    private String questKey;
    private String questName;
    private Integer target;
    private Integer progress;
    private Integer points;
    private Boolean isDone;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    /**
     * 엔티티를 DTO로 변환<br>
     * category_id는 nullable이므로, category가 null인경우 그대로 null 반환
     * @param eventQuestProgress 주간 퀘스트 진행도 엔티티
     * @return {@code EventWeeklyQuestDTO}
     */
    public static MemberEventQuestDTO fromEntity(EventQuestProgress eventQuestProgress, Integer points) {
        Category category = eventQuestProgress.getEventQuest().getCategory();
        return MemberEventQuestDTO.builder()
            .questId(eventQuestProgress.getEventQuest().getId())
            .progressId(eventQuestProgress.getId())
            .categoryId(category != null ? category.getId() : null)
            .questKey(eventQuestProgress.getEventQuest().getKey())
            .questName(eventQuestProgress.getEventQuest().getName())
            .target(eventQuestProgress.getEventQuest().getTarget())
            .progress(eventQuestProgress.getProgress())
            .points(points)
            .isDone(eventQuestProgress.getIsDone())
            .startAt(eventQuestProgress.getEventQuest().getStartDate())
            .endAt(eventQuestProgress.getEventQuest().getEndDate())
            .build();
    }
}
