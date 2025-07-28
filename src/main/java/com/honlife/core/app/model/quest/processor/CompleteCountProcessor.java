package com.honlife.core.app.model.quest.processor;

import com.honlife.core.app.model.quest.code.QuestDomain;
import com.honlife.core.infra.event.CommonEvent;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompleteCountProcessor implements QuestProcessor{

    private final RoutineScheduleRepository routineScheduleRepository;
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

        Long notCompletedRoutine = routineScheduleRepository.getCountOfNotCompletedMemberSchedule(
            LocalDate.now(), event.getMemberEmail());

        // 완료되지 않은 루틴이 있다면 return
        if(notCompletedRoutine > 0) return;

        commonQuestProcessor.updateQuestProgress(questDomain, progressId, event.getIsDone());
    }
}
