package com.honlife.core.app.model.quest.processor;

import com.honlife.core.app.model.quest.code.QuestDomain;
import com.honlife.core.infra.event.CommonEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoutineCountProcessor implements QuestProcessor{

    private final CommonQuestProcessor commonQuestProcessor;

    /**
     * 루틴 누적 완료 퀘스트 처리
     * @param event
     * @param progressId
     * @param questDomain
     */
    @Override
    @Transactional
    public void process(CommonEvent event, Long progressId,
        QuestDomain questDomain
    ) {
        // 발행된 이벤트가 루틴과 관련없는 경우
        if(event.getRoutineScheduleId()==null) {
            return;
        }

        commonQuestProcessor.updateQuestProgress(questDomain, progressId, event.getIsDone());
    }
}
