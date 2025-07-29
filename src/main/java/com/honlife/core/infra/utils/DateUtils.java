package com.honlife.core.infra.utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {

    /**
     * 입력받은 날짜를 기준으로 해당 주의 월요일 00:00:00
     * @param dateTime
     * @return
     */
    public static LocalDateTime getMondayOfWeek(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .with(LocalTime.MIN);
    }

    /**
     * 입력받은 날짜를 기준으로 해당 주의 일요일 23:59:59
     * @param dateTime
     * @return
     */
    public static LocalDateTime getSundayOfWeek(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
            .with(LocalTime.MAX);
    }
}
