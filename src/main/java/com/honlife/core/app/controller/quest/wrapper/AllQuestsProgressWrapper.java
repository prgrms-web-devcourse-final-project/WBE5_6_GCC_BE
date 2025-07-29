package com.honlife.core.app.controller.quest.wrapper;

import com.honlife.core.app.controller.quest.payload.EventQuestProgressResponse;
import com.honlife.core.app.controller.quest.payload.WeeklyQuestProgressResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AllQuestsProgressWrapper {

    private List<WeeklyQuestProgressResponse> weeklyQuests;
    private List<EventQuestProgressResponse> eventQuests;

    public AllQuestsProgressWrapper (List<WeeklyQuestProgressResponse> weeklyQuests, List<EventQuestProgressResponse> eventQuests) {
        this.weeklyQuests = weeklyQuests;
        this.eventQuests = eventQuests;
    }
}
