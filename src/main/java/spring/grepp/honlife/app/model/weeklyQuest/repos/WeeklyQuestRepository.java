package spring.grepp.honlife.app.model.weeklyQuest.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.weeklyQuest.domain.WeeklyQuest;


public interface WeeklyQuestRepository extends JpaRepository<WeeklyQuest, Integer> {
}
