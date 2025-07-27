package com.honlife.core.app.model.routine.repos;

import com.honlife.core.app.model.dashboard.dto.CategoryCountDTO;
import com.honlife.core.app.model.dashboard.dto.DayRoutineCountDTO;
import com.honlife.core.app.model.dashboard.dto.RoutineTotalCountDTO;
import java.time.LocalDate;
import java.util.List;

public interface RoutineScheduleRepositoryCustom {

    RoutineTotalCountDTO countRoutineScheduleByMemberAndDateBetweenAndIsDone(String userEmail, LocalDate localDate, LocalDate endDate);

    List<DayRoutineCountDTO> countRoutineSchedulesGroupByDateBetween(String userEmail, LocalDate localDate, LocalDate endDate);

    List<CategoryCountDTO> countRoutineSchedulesGroupByCategory(String userEmail, LocalDate startDate, LocalDate endDate, Boolean isDone);
}
