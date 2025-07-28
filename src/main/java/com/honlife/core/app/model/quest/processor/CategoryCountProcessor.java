package com.honlife.core.app.model.quest.processor;

import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.quest.code.QuestDomain;
import com.honlife.core.infra.event.CommonEvent;
import com.honlife.core.app.model.routine.repos.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryCountProcessor implements QuestProcessor {

    private final RoutineRepository routineRepository;
    private final CommonQuestProcessor commonQuestProcessor;


    /**
     * 특정 카테고리의 루틴 누적 퀘스트 처리
     * @param event
     * @param progressId
     * @param questDomain
     */
    @Override
    @Transactional
    public void process(CommonEvent event, Long progressId,
        QuestDomain questDomain
    ) {
        Long routineId = event.getRoutineId();
        // 발행된 이벤트가 루틴과 관련 없는 이벤트인경우
        if (routineId == null) {
            return;
        }

        // 카테고리 확인
        String userEmail = event.getMemberEmail();
        Category category = routineRepository.findByIdAndMember_email(routineId, userEmail).getCategory();
        CategoryType categoryType = category.getType();
        if(categoryType.equals(CategoryType.SUB)) {
            categoryType = category.getParent().getType();
        }
        if(!categoryType.equals(CategoryType.DEFAULT)) return;  // 사용자 지정 카테고리인 경우 return

        // 퀘스트와 동일 카테고리인지 확인
        Long routineCategoryId = category.getId();

        // 진행도 처리
        commonQuestProcessor.updateCategoryQuestProgress(questDomain, progressId, routineCategoryId, event.getIsDone());
    }
}
