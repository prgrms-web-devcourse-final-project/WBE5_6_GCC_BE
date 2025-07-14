package com.honlife.core.app.model.routine.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;


public interface RoutineScheduleRepository extends JpaRepository<RoutineSchedule, Long> {

    RoutineSchedule findFirstByRoutine(Routine routine);

}
