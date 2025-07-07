package spring.grepp.honlife.app.model.routine.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.routine.domain.Routine;
import spring.grepp.honlife.app.model.routine.domain.RoutineSchedule;


public interface RoutineScheduleRepository extends JpaRepository<RoutineSchedule, Integer> {

    RoutineSchedule findFirstByRoutine(Routine routine);

}
