package com.honlife.core.app.model.routine.repos;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;


public interface RoutineScheduleRepository extends JpaRepository<RoutineSchedule, Long> {

    RoutineSchedule findFirstByRoutine(Routine routine);




  RoutineSchedule findByRoutineAndDate(Routine routine, LocalDate now);

  List<RoutineSchedule> findByRoutine(Routine routine);

  boolean existsByRoutineAndDate(Routine routine, LocalDate today);
}
