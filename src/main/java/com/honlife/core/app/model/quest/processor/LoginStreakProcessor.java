package com.honlife.core.app.model.quest.processor;

import com.honlife.core.app.model.loginLog.repos.LoginLogRepository;
import com.honlife.core.app.model.quest.code.QuestDomain;
import com.honlife.core.infra.event.CommonEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginStreakProcessor implements QuestProcessor {

    private final LoginLogRepository loginLogRepository;
    private final CommonQuestProcessor commonQuestProcessor;


    /**
     * 연속 로그인 퀘스트 진행도 처리
     * @param event
     * @param progressId
     * @param questDomain
     */
    @Override
    @Transactional
    public void process(CommonEvent event, Long progressId, QuestDomain questDomain) {
        // 발행된 이벤트가 루틴과 관련되어있는 경우
        if(event.getRoutineScheduleId()!=null) {
            return;
        }

        String userEmail = event.getMemberEmail();
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);

        // 전날 로그인을 하지 않은 경우
        if(!loginLogRepository.existsByMember_EmailAndTimeBetween(userEmail, yesterday, today)) {
            commonQuestProcessor.checkAndResetProgress(questDomain, progressId);
        } else if (!loginLogRepository.existsByMember_EmailAndTimeBetween(userEmail, today, tomorrow)) {
            // 오늘 이미 로그인 한 경우
            return;
        }

        commonQuestProcessor.updateQuestProgress(questDomain, progressId, true);
    }
}
