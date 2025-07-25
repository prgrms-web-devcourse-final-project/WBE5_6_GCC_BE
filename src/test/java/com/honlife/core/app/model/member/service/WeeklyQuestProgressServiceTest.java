package com.honlife.core.app.model.member.service;

import com.honlife.core.app.model.quest.service.WeeklyQuestProgressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WeeklyQuestProgressServiceTest {

    @Autowired
    private WeeklyQuestProgressService weeklyQuestProgressService;

    @Test
    void deleteMemberQuestByMemberId() {
        weeklyQuestProgressService.softDropMemberQuestByMemberId(3L);
    }

}