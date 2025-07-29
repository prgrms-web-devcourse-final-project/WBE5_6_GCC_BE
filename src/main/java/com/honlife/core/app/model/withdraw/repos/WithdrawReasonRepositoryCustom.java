package com.honlife.core.app.model.withdraw.repos;

import com.honlife.core.app.model.withdraw.code.WithdrawType;
import com.honlife.core.app.model.withdraw.domain.WithdrawReason;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WithdrawReasonRepositoryCustom {

    Page<WithdrawReason> findPagedByDateBetween(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);

    Map<WithdrawType, Long> countByType(LocalDateTime startDate, LocalDateTime endDate);

    // === 일간 통계 ===
    /**
     * 날짜별 탈퇴자 수 조회
     * @param startDateTime 시작 날짜시간
     * @param endDateTime 종료 날짜시간
     * @return List<Object[]> - [날짜, 탈퇴자 수]
     */
    List<Object[]> findWithdrawalsByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime);

    // === 주간 통계 ===
    /**
     * 주별 탈퇴자 수 조회
     * @param startDateTime 시작 날짜시간
     * @param endDateTime 종료 날짜시간
     * @return List<Object[]> - [주의 첫날, 해당 주 탈퇴자 수]
     */
    List<Object[]> findWithdrawalsByWeek(LocalDateTime startDateTime, LocalDateTime endDateTime);

    // === 월간 통계 ===
    /**
     * 월별 탈퇴자 수 조회
     * @param startDateTime 시작 날짜시간
     * @param endDateTime 종료 날짜시간
     * @return List<Object[]> - [월의 첫날, 해당 월 탈퇴자 수]
     */
    List<Object[]> findWithdrawalsByMonth(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
