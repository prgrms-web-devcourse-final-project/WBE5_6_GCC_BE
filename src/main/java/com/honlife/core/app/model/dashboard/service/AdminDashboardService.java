package com.honlife.core.app.model.dashboard.service;

import com.honlife.core.app.model.dashboard.code.PeriodType;
import com.honlife.core.app.model.dashboard.dto.AdminDashboardStatsDTO;
import com.honlife.core.app.model.dashboard.dto.DailyStatDTO;
import com.honlife.core.app.model.loginLog.repos.LoginLogRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.withdraw.repos.WithdrawReasonRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final MemberRepository memberRepository;
    private final LoginLogRepository loginLogRepository;
    private final WithdrawReasonRepository withdrawReasonRepository;

    /**
     * 관리자 대시보드 통계 데이터를 조회합니다.
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @param periodType 집계 단위 (DAILY, WEEKLY, MONTHLY)
     * @return AdminDashboardStatsDTO
     */
    public AdminDashboardStatsDTO getDashboardStats(LocalDate startDate, LocalDate endDate, PeriodType periodType) {
        // 기본값 처리
        if (startDate == null || endDate == null) {
            LocalDate today = LocalDate.now();
            switch (periodType) {
                case WEEKLY -> {
                    startDate = today.minusWeeks(4);  // 최근 4주
                    endDate = today;
                }
                case MONTHLY -> {
                    startDate = today.minusMonths(6); // 최근 6개월
                    endDate = today;
                }
                default -> {
                    startDate = today.minusDays(7);   // 최근 7일
                    endDate = today;
                }
            }
        }

        return AdminDashboardStatsDTO.builder()
            .totalMembers(getTotalMembersStats(startDate, endDate, periodType))
            .newMembers(getNewMembersStats(startDate, endDate, periodType))
            .visitors(getVisitorsStats(startDate, endDate, periodType))
            .withdrawals(getWithdrawalsStats(startDate, endDate, periodType))
            .build();
    }

    /**
     * 기간별 총 회원 수 (누적) 조회
     */
    private List<DailyStatDTO> getTotalMembersStats(LocalDate startDate, LocalDate endDate, PeriodType periodType) {
        List<Object[]> results;

        switch (periodType) {
            case WEEKLY -> results = memberRepository.findTotalMembersByWeek(startDate, endDate);
            case MONTHLY -> results = memberRepository.findTotalMembersByMonth(startDate, endDate);
            default -> results = memberRepository.findTotalMembersByDateRange(startDate, endDate);
        }

        return results.stream()
            .map(result -> DailyStatDTO.builder()
                .date((LocalDate) result[0])
                .count((Long) result[1])
                .build())
            .collect(Collectors.toList());
    }

    /**
     * 기간별 신규 회원 수 조회
     */
    private List<DailyStatDTO> getNewMembersStats(LocalDate startDate, LocalDate endDate, PeriodType periodType) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Object[]> results;

        switch (periodType) {
            case WEEKLY -> results = memberRepository.findNewMembersByWeek(startDateTime, endDateTime);
            case MONTHLY -> results = memberRepository.findNewMembersByMonth(startDateTime, endDateTime);
            default -> results = memberRepository.findNewMembersByDateRange(startDateTime, endDateTime);
        }

        return results.stream()
            .map(result -> DailyStatDTO.builder()
                .date((LocalDate) result[0])
                .count((Long) result[1])
                .build())
            .collect(Collectors.toList());
    }

    /**
     * 기간별 방문자 수 (고유 사용자) 조회
     */
    private List<DailyStatDTO> getVisitorsStats(LocalDate startDate, LocalDate endDate, PeriodType periodType) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Object[]> results;

        switch (periodType) {
            case WEEKLY -> results = loginLogRepository.findVisitorsByWeek(startDateTime, endDateTime);
            case MONTHLY -> results = loginLogRepository.findVisitorsByMonth(startDateTime, endDateTime);
            default -> results = loginLogRepository.findVisitorsByDateRange(startDateTime, endDateTime);
        }

        return results.stream()
            .map(result -> DailyStatDTO.builder()
                .date((LocalDate) result[0])
                .count((Long) result[1])
                .build())
            .collect(Collectors.toList());
    }

    /**
     * 기간별 탈퇴자 수 조회
     */
    private List<DailyStatDTO> getWithdrawalsStats(LocalDate startDate, LocalDate endDate, PeriodType periodType) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Object[]> results;

        switch (periodType) {
            case WEEKLY -> results = withdrawReasonRepository.findWithdrawalsByWeek(startDateTime, endDateTime);
            case MONTHLY -> results = withdrawReasonRepository.findWithdrawalsByMonth(startDateTime, endDateTime);
            default -> results = withdrawReasonRepository.findWithdrawalsByDateRange(startDateTime, endDateTime);
        }

        return results.stream()
            .map(result -> DailyStatDTO.builder()
                .date((LocalDate) result[0])
                .count((Long) result[1])
                .build())
            .collect(Collectors.toList());
    }
}
