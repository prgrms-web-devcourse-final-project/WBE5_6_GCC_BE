package com.honlife.core.app.model.quest.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.quest.domain.WeeklyQuest;


public interface WeeklyQuestRepository extends JpaRepository<WeeklyQuest, Long> {
}
