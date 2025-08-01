package com.honlife.core.app.model.quest.processor;

import com.honlife.core.app.model.quest.code.QuestDomain;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
import com.honlife.core.infra.event.CommonEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompleteStreakProcessor implements QuestProcessor {

    private final RoutineScheduleRepository routineScheduleRepository;
    private final CompleteCountProcessor completeCountProcessor;
    private final CommonQuestProcessor commonQuestProcessor;

    @Override
    @Transactional
    public void process(CommonEvent event, Long progressId,
        QuestDomain questDomain
    ) {
        // 발행된 이벤트가 루틴과 관련 없는 이벤트인경우
        if (event.getRoutineScheduleId() == null) {
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        String userEmail = event.getMemberEmail();

        // 월요일이 아니라면 전날 루틴이 100%달성인지 확인
        // 아니라면 진행도 초기화
        if (
            (today != monday)
                && (routineScheduleRepository
                .getCountOfNotCompletedMemberSchedule(today.minusDays(1), userEmail) > 0)
                && event.getIsDone()
        ) {
            commonQuestProcessor.checkAndResetProgress(questDomain, progressId);
        }

        // 월요일이거나 전날 루틴 100%달성인 경우
        completeCountProcessor.process(event, progressId, questDomain);
    }
}
