package com.honlife.core.app.model.quest.repos;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.quest.domain.EventQuest;


public interface EventQuestRepository extends JpaRepository<EventQuest, Long> {

    List<EventQuest> findAllByEndDateLessThanEqualAndIsActiveTrue(LocalDateTime endDateIsLessThan);

    List<EventQuest> findAllByEndDateGreaterThanEqualAndStartDateLessThanEqualAndIsActiveFalse(
        LocalDateTime endDateIsGreaterThan,
        LocalDateTime startDateIsLessThan);
}
