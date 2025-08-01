package com.honlife.core.app.model.routine.repos;

import java.time.LocalDate;
import com.honlife.core.app.model.dashboard.dto.CategoryCountDTO;
import com.honlife.core.app.model.dashboard.dto.DayRoutineCountDTO;
import com.honlife.core.app.model.dashboard.dto.RoutineTotalCountDTO;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import java.time.LocalDate;
import java.util.List;

public interface RoutineScheduleRepositoryCustom {

    /**
     * 특정 날짜에 대해 회원이 완료하지 않은 루틴의 갯수를 반환
     * @param date
     * @param userEmail
     */
    Long getCountOfNotCompletedMemberSchedule(LocalDate date, String userEmail);

    RoutineTotalCountDTO countRoutineScheduleByMemberAndDateBetweenAndIsDone(String userEmail, LocalDate localDate, LocalDate endDate);

    List<DayRoutineCountDTO> countRoutineSchedulesGroupByDateBetween(String userEmail, LocalDate localDate, LocalDate endDate);

    List<CategoryCountDTO> countRoutineSchedulesGroupByCategory(String userEmail, LocalDate startDate, LocalDate endDate, Boolean isDone);

    List<RoutineSchedule> findAllByDateBetween(String userEmail, LocalDate startDate, LocalDate endDate);
}