package com.honlife.core.app.model.quest.scheduler;

import com.honlife.core.app.model.quest.service.EventQuestProgressService;
import com.honlife.core.app.model.quest.service.EventQuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventQuestScheduler {

    private final EventQuestService eventQuestService;
    private final EventQuestProgressService eventQuestProgressService;

    @Async
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void updateEventQuestsProgress() {
        eventQuestProgressService.deactivateOldEventQuests();
    }
}
