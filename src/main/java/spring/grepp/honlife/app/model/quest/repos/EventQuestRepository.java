package spring.grepp.honlife.app.model.quest.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.quest.domain.EventQuest;


public interface EventQuestRepository extends JpaRepository<EventQuest, Long> {
}
