package com.honlife.core.app.model.member.repos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepositoryCustom {

    /**
     * 이메일 인증이 완료된 상태의 계정인지 확인합니다.
     * @param email 검증할 이메일
     * @return {@code Boolean}
     */
    boolean isEmailVerified(String email);

    /**
     * 멤버 소프트 드랍
     * @param userEmail 유저 이메일
     */
    void softDropMember(String userEmail);

    // === 일간 통계 ===
    /**
     * 날짜별 총 회원 수 (누적) 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return List<Object[]> - [날짜, 누적 회원 수]
     */
    List<Object[]> findTotalMembersByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * 날짜별 신규 회원 수 조회
     * @param startDateTime 시작 날짜시간
     * @param endDateTime 종료 날짜시간
     * @return List<Object[]> - [날짜, 신규 회원 수]
     */
    List<Object[]> findNewMembersByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime);

// === 주간 통계 ===
    /**
     * 주별 총 회원 수 (누적) 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return List<Object[]> - [주의 첫날, 해당 주까지의 누적 회원 수]
     */
    List<Object[]> findTotalMembersByWeek(LocalDate startDate, LocalDate endDate);

    /**
     * 주별 신규 회원 수 조회
     * @param startDateTime 시작 날짜시간
     * @param endDateTime 종료 날짜시간
     * @return List<Object[]> - [주의 첫날, 해당 주 신규 회원 수]
     */
    List<Object[]> findNewMembersByWeek(LocalDateTime startDateTime, LocalDateTime endDateTime);

// === 월간 통계 ===
    /**
     * 월별 총 회원 수 (누적) 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return List<Object[]> - [월의 첫날, 해당 월까지의 누적 회원 수]
     */
    List<Object[]> findTotalMembersByMonth(LocalDate startDate, LocalDate endDate);

    /**
     * 월별 신규 회원 수 조회
     * @param startDateTime 시작 날짜시간
     * @param endDateTime 종료 날짜시간
     * @return List<Object[]> - [월의 첫날, 해당 월 신규 회원 수]
     */
    List<Object[]> findNewMembersByMonth(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
