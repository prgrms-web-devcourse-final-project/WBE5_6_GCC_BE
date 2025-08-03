package com.honlife.core.app.model.quest.router;

import com.honlife.core.app.model.quest.code.QuestDomain;
import com.honlife.core.app.model.quest.code.QuestType;
import com.honlife.core.app.model.quest.processor.CategoryCountProcessor;
import com.honlife.core.app.model.quest.processor.CompleteCountProcessor;
import com.honlife.core.app.model.quest.processor.CompleteStreakProcessor;
import com.honlife.core.app.model.quest.processor.LoginCountProcessor;
import com.honlife.core.app.model.quest.processor.LoginStreakProcessor;
import com.honlife.core.app.model.quest.processor.QuestProcessor;
import com.honlife.core.app.model.quest.processor.RoutineCountProcessor;
import com.honlife.core.infra.event.CommonEvent;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QuestProgressProcessorRouter {

    private final Map<QuestType, QuestProcessor> processorMap;

    /**
     * 프로세서 생성자로서, 퀘스트 타입에 따라 프로세서를 매핑
     * @param processors
     */
    public QuestProgressProcessorRouter(List<QuestProcessor> processors) {
        this.processorMap = Map.of(
            QuestType.ROUTINE_COUNT, findProcessor(processors, RoutineCountProcessor.class),
            QuestType.CATEGORY_COUNT, findProcessor(processors, CategoryCountProcessor.class),
            QuestType.COMPLETE_COUNT, findProcessor(processors, CompleteCountProcessor.class),
            QuestType.COMPLETE_STREAK, findProcessor(processors, CompleteStreakProcessor.class),
            QuestType.LOGIN_COUNT, findProcessor(processors, LoginCountProcessor.class),
            QuestType.LOGIN_STREAK, findProcessor(processors, LoginStreakProcessor.class)
        );
    }

    /**
     * 적절한 프로세서로 라우팅 후 처리 위임
     * @param domainType
     * @param questType
     * @param event
     * @param progressId
     */
    public void routeAndProcess(QuestDomain domainType, QuestType questType, CommonEvent event, Long progressId) {
        log.info("QuestProgressProcessorRouter :: routeAndProcess() - domain_type: {}, quest_type: {}, progress_id: {}", domainType, questType, progressId);
        QuestProcessor processor = processorMap.get(questType);
        if (processor == null) {
            throw new IllegalArgumentException("지원하지 않는 도메인/타입 조합입니다.");
        }
        processor.process(event, progressId, domainType);
    }

    /**
     * 매핑된 프로세서중 적절한 프로세서를 탐색
     * @param processors
     * @param clazz
     * @return
     */
    private QuestProcessor findProcessor(List<QuestProcessor> processors, Class<?> clazz) {
        return processors.stream()
            .filter(clazz::isInstance)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Processor not found"));
    }
}
