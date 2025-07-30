package com.honlife.core.app.model.quest.listener;

import com.honlife.core.app.model.quest.service.EventQuestProgressService;
import com.honlife.core.app.model.quest.service.WeeklyQuestProgressService;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.honlife.core.infra.event.CommonEvent;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoutineEventListener {

    private final WeeklyQuestProgressService weeklyQuestProgressService;
    private final EventQuestProgressService eventQuestProgressService;
    private final RoutineScheduleRepository scheduleRepository;

    /**
     * 루틴 완료시 퀘스트 진행도 처리를 수행하는 매서드<br>
     * 주간, 이벤트 퀘스트 서비스 레이어에 작업을 분배하고 빠르게 종료됩니다.
     * @param event 이벤트 처리에 필요한 정보를 담은 객체
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRoutineCompletionChanged(CommonEvent event) {

        log.info("handleRoutineCompletionChanged() :: [Quest] Routine event handler got event");

        // 루틴 관련 이벤트가 아닌 경우 리턴
        Long routineScheduleId = event.getRoutineScheduleId();
        if(routineScheduleId == null) {
            return;
        }

        RoutineSchedule routineSchedule = scheduleRepository.findById(routineScheduleId)
            .orElseThrow();

        // 루틴이 완료되지 않은 상태인 경우
        event.setIsDone(routineSchedule.getIsDone());

        // 루틴 완료 검증이 끝난 경우 주간, 이벤트 퀘스트에서 진행도 처리
        weeklyQuestProgressService.processWeeklyQuestProgress(event);
        eventQuestProgressService.processEventQuestProgress(event);
    }
}
