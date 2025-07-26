package com.honlife.core.app.model.quest.scheduler;

import com.honlife.core.app.model.quest.service.WeeklyQuestProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeeklyQuestScheduler {

    private final WeeklyQuestProgressService weeklyQuestProgressService;

    /**
     * 매주 월요일 0시에 실행 (cron: 초 분 시 일 월 요일)
     * 매주 월요일 0시 0분 0초
     */
    @Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Seoul")
    public void deactivatePreviousWeeklyQuests() {
        weeklyQuestProgressService.deactivatePreviousWeeklyQuests();
    }
}
