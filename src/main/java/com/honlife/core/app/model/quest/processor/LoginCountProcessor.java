package com.honlife.core.app.model.quest.processor;

import com.honlife.core.app.model.loginLog.repos.LoginLogRepository;
import com.honlife.core.app.model.quest.code.QuestDomain;
import com.honlife.core.infra.event.CommonEvent;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginCountProcessor implements QuestProcessor {

    private final CommonQuestProcessor commonQuestProcessor;
    private final LoginLogRepository loginLogRepository;

    @Override
    @Transactional
    public void process(CommonEvent event, Long progressId, QuestDomain questDomain) {
        // 발행된 이벤트가 루틴과 관련되어있는 경우
        if(event.getRoutineScheduleId()!=null) {
            return;
        }

        // 오늘 이미 로그인 한 경우
        String userEmail = event.getMemberEmail();
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tomorrow = today.plusDays(1);
        if(loginLogRepository.findByMember_EmailAndTimeBetween(userEmail, today, tomorrow).isPresent()) return;

        commonQuestProcessor.updateQuestProgress(questDomain, progressId, true);
    }
}
