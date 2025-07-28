package com.honlife.core.app.model.routine.repos;

import java.time.LocalDate;

public interface RoutineScheduleRepositoryCustom {

    /**
     * 특정 날짜에 대해 회원이 완료하지 않은 루틴의 갯수를 반환
     * @param date
     * @param userEmail
     */
    Long getCountOfNotCompletedMemberSchedule(LocalDate date, String userEmail);

}
