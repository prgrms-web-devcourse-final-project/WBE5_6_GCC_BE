package com.honlife.core.app.model.quest.listener;

import com.honlife.core.app.model.quest.service.EventQuestProgressService;
import com.honlife.core.app.model.quest.service.WeeklyQuestProgressService;
import com.honlife.core.infra.event.CommonEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginEventListener {

    private final WeeklyQuestProgressService weeklyQuestProgressService;
    private final EventQuestProgressService eventQuestProgressService;

    @EventListener
    public void handleOnLogin(CommonEvent event) {

        log.info("handleOnLogin() :: [Quest] Login event handler got event");

        // 루틴 관련 이벤트라면 return
        if(event.getRoutineScheduleId() != null) return;

        // 로그인의 경우에는 이번주의 주간퀘스트 갱신이 제대로 이루어 졌는지 확인하고 퀘스트 진행도 업데이트를 진행하도록 강제
        weeklyQuestProgressService.renewWeeklyQuests(event)
                .thenRun(() -> {
                    weeklyQuestProgressService.processWeeklyQuestProgress(event);
                });
        eventQuestProgressService.processEventQuestProgress(event);
    }
}
