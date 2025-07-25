package com.honlife.core.app.model.quest.repos;

import com.honlife.core.app.model.quest.domain.EventQuestProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventQuestProgressRepository extends JpaRepository<EventQuestProgress, Long>, EventQuestProgressRepositoryCustom {

}
