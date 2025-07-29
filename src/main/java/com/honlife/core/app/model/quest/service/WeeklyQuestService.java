package com.honlife.core.app.model.quest.service;

import com.honlife.core.app.model.quest.domain.WeeklyQuest;
import com.honlife.core.app.model.quest.repos.WeeklyQuestRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeeklyQuestService {

    private final WeeklyQuestRepository weeklyQuestRepository;

    /**
     * DB에 존재하는 주간 퀘스트 목록에서 count 만큼 랜덤한 퀘스트를 조회후 반환
     * @param count 얻고자하는 퀘스트 개수
     * @return List of {@link WeeklyQuest}
     */
    public List<WeeklyQuest> getRandomQuests(int count) {
        return weeklyQuestRepository.getRandomWeeklyQuest(count);
    }
}
