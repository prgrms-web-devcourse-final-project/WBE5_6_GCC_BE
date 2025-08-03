package com.honlife.core.app.model.quest.service;

import com.honlife.core.app.model.quest.domain.EventQuest;
import com.honlife.core.app.model.quest.repos.EventQuestRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventQuestService {

    private final EventQuestRepository eventQuestRepository;

    /**
     * 특정 날짜를 기간으로 진행중인 이벤트의 목록을 반환하는 매서드
     * @param dateTime
     */
    public List<EventQuest> findNewQuests(LocalDateTime dateTime) {
        return eventQuestRepository.findAllByEndDateGreaterThanEqualAndStartDateLessThanEqualAndIsActiveFalse(dateTime, dateTime);
    }

    public List<EventQuest> findAllOldEventQuests(LocalDateTime dateTime) {
        return eventQuestRepository.findAllByEndDateLessThanEqualAndIsActiveTrue(dateTime);
    }
}
