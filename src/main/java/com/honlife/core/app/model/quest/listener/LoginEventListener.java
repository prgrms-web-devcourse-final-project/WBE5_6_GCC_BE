package com.honlife.core.app.model.quest.listener;

import com.honlife.core.app.model.quest.service.EventQuestProgressService;
import com.honlife.core.app.model.quest.service.WeeklyQuestProgressService;
import com.honlife.core.infra.event.CommonEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginEventListener {

    private final WeeklyQuestProgressService weeklyQuestProgressService;
    private final EventQuestProgressService eventQuestProgressService;

    @EventListener
    public void handleOnLogin(CommonEvent event) {
        // 루틴 관련 이벤트라면 return
        if(event.getRoutineScheduleId() != null) return;

        weeklyQuestProgressService.processWeeklyQuestProgress(event);
        eventQuestProgressService.processEventQuestProgress(event);
    }
}
