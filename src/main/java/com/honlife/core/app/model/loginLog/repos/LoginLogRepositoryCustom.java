package com.honlife.core.app.model.loginLog.repos;

import java.time.LocalDateTime;
import java.util.List;

public interface LoginLogRepositoryCustom {

    // === 일간 통계 ===
    /**
     * 날짜별 방문자 수 (고유 사용자) 조회
     * @param startDateTime 시작 날짜시간
     * @param endDateTime 종료 날짜시간
     * @return List<Object[]> - [날짜, 방문자 수]
     */
    List<Object[]> findVisitorsByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime);

    // === 주간 통계 ===
    /**
     * 주별 방문자 수 (고유 사용자) 조회
     * @param startDateTime 시작 날짜시간
     * @param endDateTime 종료 날짜시간
     * @return List<Object[]> - [주의 첫날, 해당 주 방문자 수]
     */
    List<Object[]> findVisitorsByWeek(LocalDateTime startDateTime, LocalDateTime endDateTime);

    // === 월간 통계 ===
    /**
     * 월별 방문자 수 (고유 사용자) 조회
     * @param startDateTime 시작 날짜시간
     * @param endDateTime 종료 날짜시간
     * @return List<Object[]> - [월의 첫날, 해당 월 방문자 수]
     */
    List<Object[]> findVisitorsByMonth(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
