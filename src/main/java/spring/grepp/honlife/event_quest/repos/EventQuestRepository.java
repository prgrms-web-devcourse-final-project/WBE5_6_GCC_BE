package spring.grepp.honlife.event_quest.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.event_quest.domain.EventQuest;


public interface EventQuestRepository extends JpaRepository<EventQuest, Integer> {
}
