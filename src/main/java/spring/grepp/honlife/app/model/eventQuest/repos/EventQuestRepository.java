package spring.grepp.honlife.app.model.eventQuest.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.eventQuest.domain.EventQuest;


public interface EventQuestRepository extends JpaRepository<EventQuest, Integer> {
}
