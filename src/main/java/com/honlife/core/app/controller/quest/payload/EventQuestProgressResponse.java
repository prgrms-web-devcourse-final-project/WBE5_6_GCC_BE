package com.honlife.core.app.controller.quest.payload;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventQuestProgressResponse {

    private Long questId;
    private Long progressId;
    private Long categoryId;
    private String questKey;
    private String questName;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer target;
    private Integer progress;
}
