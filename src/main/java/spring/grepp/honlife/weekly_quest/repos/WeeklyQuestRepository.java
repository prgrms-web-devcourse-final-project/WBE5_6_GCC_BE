package spring.grepp.honlife.weekly_quest.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.weekly_quest.domain.WeeklyQuest;


public interface WeeklyQuestRepository extends JpaRepository<WeeklyQuest, Integer> {
}
