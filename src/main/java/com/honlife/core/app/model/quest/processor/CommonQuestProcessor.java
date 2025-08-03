package com.honlife.core.app.model.quest.processor;

import com.honlife.core.app.model.notification.code.NotificationType;
import com.honlife.core.app.model.notification.service.NotifyListService;
import com.honlife.core.app.model.notification.service.NotifyPublisher;
import com.honlife.core.app.model.quest.code.QuestDomain;
import com.honlife.core.app.model.quest.domain.EventQuestProgress;
import com.honlife.core.app.model.quest.domain.WeeklyQuestProgress;
import com.honlife.core.app.model.quest.repos.EventQuestProgressRepository;
import com.honlife.core.app.model.quest.repos.WeeklyQuestProgressRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommonQuestProcessor {

    private final WeeklyQuestProgressRepository weeklyQuestProgressRepository;
    private final EventQuestProgressRepository eventQuestProgressRepository;
    private final NotifyPublisher notifyPublisher;


    /**
     * 단순 진행도를 처리하는 매서드
     *
     * @param questDomain 퀘스트 도메인 종류
     * @param progressId  진행도 데이터 id
     * @param isDone      루틴 완료 여부
     */
    @Transactional
    public void updateQuestProgress(QuestDomain questDomain, Long progressId, Boolean isDone) {
        if (questDomain.equals(QuestDomain.WEEKLY)) {
            WeeklyQuestProgress progress = weeklyQuestProgressRepository.findById(progressId)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
            Integer target = progress.getWeeklyQuest().getTarget();
            updateProgress(progress.getProgress(), target, isDone, progress::setProgress);
            checkAndSendSocket(progress, target);
        } else if (questDomain.equals(QuestDomain.EVENT)) {
            EventQuestProgress progress = eventQuestProgressRepository.findById(progressId)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
            Integer target = progress.getEventQuest().getTarget();
            updateProgress(progress.getProgress(), target, isDone, progress::setProgress);
            checkAndSendSocket(progress, target);
        }
    }

    @Transactional
    protected void checkAndSendSocket(EventQuestProgress progress, Integer target) {
        if(progress.getProgress().equals(target)) {
            String userEmail = progress.getMember().getEmail();
            notifyPublisher.saveNotifyAndSendSse(userEmail, progress.getEventQuest().getName(), NotificationType.QUEST);
        }
    }

    @Transactional
    protected void checkAndSendSocket(WeeklyQuestProgress progress, Integer target) {
        if(progress.getProgress().equals(target)) {
            String userEmail = progress.getMember().getEmail();
            notifyPublisher.saveNotifyAndSendSse(userEmail, progress.getWeeklyQuest().getName(), NotificationType.QUEST);
        }
    }

    /**
     * 루틴의 카테고리와 퀘스트의 카테고리가 같을 때 진행도를 처리하는 매서드
     *
     * @param questDomain       퀘스트 도메인 종류
     * @param progressId        진행도 데이터 id
     * @param routineCategoryId 루틴의 카테고리 id
     * @param isDone            루틴 완료 여부
     */
    @Transactional
    public void updateCategoryQuestProgress(QuestDomain questDomain, Long progressId,
        Long routineCategoryId, Boolean isDone) {
        if (questDomain.equals(QuestDomain.WEEKLY)) {
            WeeklyQuestProgress progress = weeklyQuestProgressRepository.findById(progressId)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
            Long questCategoryId = progress.getWeeklyQuest().getCategory().getId();
            if (questCategoryId.equals(routineCategoryId)) {
                Integer target = progress.getWeeklyQuest().getTarget();
                updateProgress(progress.getProgress(), target, isDone, progress::setProgress);
            }
        } else if (questDomain.equals(QuestDomain.EVENT)) {
            EventQuestProgress progress = eventQuestProgressRepository.findById(progressId)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
            Long questCategoryId = progress.getEventQuest().getCategory().getId();
            if (questCategoryId.equals(routineCategoryId)) {
                Integer target = progress.getEventQuest().getTarget();
                updateProgress(progress.getProgress(), target, isDone, progress::setProgress);
            }
        }
    }

    /**
     * 연속달성 퀘스트 달성도 초기화<br> 만약 이미 목표치를 달성한 경우가 아니라면 초기화.
     *
     * @param questDomain
     * @param progressId
     */
    @Transactional
    public void checkAndResetProgress(QuestDomain questDomain, Long progressId) {
        if (questDomain.equals(QuestDomain.WEEKLY)) {
            WeeklyQuestProgress progress = weeklyQuestProgressRepository.findById(progressId)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
            Integer currentProgress = progress.getProgress();
            Integer target = progress.getWeeklyQuest().getTarget();
            if (!currentProgress.equals(target)) {
                progress.setProgress(0);
            }
        } else if (questDomain.equals(QuestDomain.EVENT)) {
            EventQuestProgress progress = eventQuestProgressRepository.findById(progressId)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
            Integer currentProgress = progress.getProgress();
            Integer target = progress.getEventQuest().getTarget();
            if (!currentProgress.equals(target)) {
                progress.setProgress(0);
            }
        }
    }

    /**
     * 루틴 완료 여부에 따라 진행도를 올리거나 내리는 매서드
     *
     * @param currentProgress 현재의 진행도
     * @param target          목표치
     * @param isDone          루틴 완료 여부
     * @param setter          도메인 객체의 set 매서드를 받는 변수
     */
    public static void updateProgress(int currentProgress, int target, boolean isDone,
        Consumer<Integer> setter) {
        // 이미 기준을 충족한 경우
        if (currentProgress == target) {
            return;
        }

        int progress = currentProgress;
        if (!isDone) {
            progress--;
        } else {
            progress++;
        }
        setter.accept(progress);
    }

}
