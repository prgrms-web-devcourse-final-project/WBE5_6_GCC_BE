package com.honlife.core.app.controller.admin.dashboard;

import com.honlife.core.app.controller.admin.dashboard.payload.AdminDashboardStatsResponse;
import com.honlife.core.app.controller.admin.dashboard.payload.DailyStatResponse;
import com.honlife.core.app.model.dashboard.PeriodType;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name="🔄 [관리자] 대시보드", description = "관리자 대시보드 통계 API입니다.")
@RestController
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(value = "/api/v1/admin/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDashboardController {

    /**
     * 관리자 대시보드 통계 조회 API
     * @param startDate 시작 날짜 (선택사항)
     * @param endDate 종료 날짜 (선택사항)
     * @param periodType 집계 단위 (DAILY/WEEKLY/MONTHLY)
     * @return 대시보드 통계 데이터
     */
    @Operation(
        summary = "🔄 관리자 대시보드 통계 조회",
        description = "관리자 대시보드에서 사용할 통계 데이터를 조회합니다. " +
            "<br><br>**📊 제공하는 통계:**" +
            "<br>• **총 회원 수**: 해당 날짜까지의 누적 활성 회원 수" +
            "<br>• **신규 회원 수**: 해당 기간에 새로 가입한 회원 수" +
            "<br>• **방문자 수**: 해당 기간에 로그인한 고유 사용자 수" +
            "<br>• **탈퇴자 수**: 해당 기간에 탈퇴한 회원 수" +
            "<br><br>**⏰ 집계 단위:**" +
            "<br>• **DAILY**: 일간 통계 (기본: 최근 7일)" +
            "<br>• **WEEKLY**: 주간 통계 (기본: 최근 4주, 월요일 기준)" +
            "<br>• **MONTHLY**: 월간 통계 (기본: 최근 6개월, 매월 1일 기준)" +
            "<br><br>**📈 프론트엔드 활용:**" +
            "<br>• Chart.js, D3.js 등 차트 라이브러리와 호환" +
            "<br>• X축: date 배열, Y축: count 배열로 바로 사용 가능" +
            "<br>• 4개 지표를 하나의 멀티라인 차트로 표시 권장" +
            "<br><br>**🔒 권한:**" +
            "<br>• ADMIN 권한 필요 (Bearer Token)"
    )
    @GetMapping("/stats")
    public ResponseEntity<CommonApiResponse<AdminDashboardStatsResponse>> getDashboardStats(
        @Parameter(description = "시작 날짜 (없으면 자동 설정)", example = "2025-07-01")
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,

        @Parameter(description = "종료 날짜 (없으면 자동 설정)", example = "2025-07-28")
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,

        @Parameter(description = "집계 단위", example = "DAILY")
        @RequestParam(defaultValue = "DAILY") PeriodType periodType
    ) {

        // 기본값 처리
        if (startDate == null || endDate == null) {
            LocalDate today = LocalDate.now();
            switch (periodType) {
                case WEEKLY -> {
                    startDate = today.minusWeeks(4);
                    endDate = today;
                }
                case MONTHLY -> {
                    startDate = today.minusMonths(6);
                    endDate = today;
                }
                default -> {
                    startDate = today.minusDays(7);
                    endDate = today;
                }
            }
        }

        // 모킹 데이터 생성
        AdminDashboardStatsResponse mockResponse = generateMockResponse(periodType, startDate, endDate);

        return ResponseEntity.ok(CommonApiResponse.success(mockResponse));
    }

    /**
     * 모킹 응답 데이터 생성
     */
    private AdminDashboardStatsResponse generateMockResponse(PeriodType periodType, LocalDate startDate, LocalDate endDate) {
        List<DailyStatResponse> totalMembers = new ArrayList<>();
        List<DailyStatResponse> newMembers = new ArrayList<>();
        List<DailyStatResponse> visitors = new ArrayList<>();
        List<DailyStatResponse> withdrawals = new ArrayList<>();

        switch (periodType) {
            case DAILY -> generateDailyMockData(totalMembers, newMembers, visitors, withdrawals, startDate, endDate);
            case WEEKLY -> generateWeeklyMockData(totalMembers, newMembers, visitors, withdrawals, startDate, endDate);
            case MONTHLY -> generateMonthlyMockData(totalMembers, newMembers, visitors, withdrawals, startDate, endDate);
        }

        return AdminDashboardStatsResponse.builder()
            .totalMembers(totalMembers)
            .newMembers(newMembers)
            .visitors(visitors)
            .withdrawals(withdrawals)
            .build();
    }

    /**
     * 일간 모킹 데이터 생성 (루틴 관리 서비스 패턴)
     */
    private void generateDailyMockData(List<DailyStatResponse> totalMembers, List<DailyStatResponse> newMembers,
        List<DailyStatResponse> visitors, List<DailyStatResponse> withdrawals,
        LocalDate startDate, LocalDate endDate) {
        LocalDate current = startDate;
        long totalCount = 6850L; // 시작 회원 수

        while (!current.isAfter(endDate)) {
            // 루틴 앱 특성: 월요일 시작 동기부여 높음, 주말 사용량 감소
            int dayOfWeek = current.getDayOfWeek().getValue(); // 1=월요일, 7=일요일

            long newMemberCount;
            long visitorCount;

            if (dayOfWeek == 1) { // 월요일: 새로운 다짐
                newMemberCount = (long) (Math.random() * 8) + 12; // 12~19명
                visitorCount = (long) (Math.random() * 40) + 60;  // 60~99명
            } else if (dayOfWeek <= 5) { // 화~금: 평일 루틴 관리
                newMemberCount = (long) (Math.random() * 6) + 8;  // 8~13명
                visitorCount = (long) (Math.random() * 30) + 45;  // 45~74명
            } else { // 토~일: 주말 휴식
                newMemberCount = (long) (Math.random() * 4) + 3;  // 3~6명
                visitorCount = (long) (Math.random() * 20) + 25;  // 25~44명
            }

            long withdrawalCount = (long) (Math.random() * 2) + 1; // 1~2명 (적은 이탈률)

            totalCount += newMemberCount;

            totalMembers.add(DailyStatResponse.builder().date(current).count(totalCount).build());
            newMembers.add(DailyStatResponse.builder().date(current).count(newMemberCount).build());
            visitors.add(DailyStatResponse.builder().date(current).count(visitorCount).build());
            withdrawals.add(DailyStatResponse.builder().date(current).count(withdrawalCount).build());

            current = current.plusDays(1);
        }
    }

    /**
     * 주간 모킹 데이터 생성 (루틴 관리 서비스 패턴)
     */
    private void generateWeeklyMockData(List<DailyStatResponse> totalMembers, List<DailyStatResponse> newMembers,
        List<DailyStatResponse> visitors, List<DailyStatResponse> withdrawals,
        LocalDate startDate, LocalDate endDate) {
        LocalDate current = startDate;
        long totalCount = 6200L;

        while (!current.isAfter(endDate)) {
            // 주의 첫날 (월요일)로 조정
            LocalDate weekStart = current.minusDays(current.getDayOfWeek().getValue() - 1);

            // 루틴 앱: 주간 단위로 목표 설정하는 사용자 많음
            long newMemberCount = (long) (Math.random() * 30) + 40; // 40~69명
            long visitorCount = (long) (Math.random() * 150) + 200; // 200~349명
            long withdrawalCount = (long) (Math.random() * 6) + 4; // 4~9명

            totalCount += newMemberCount;

            totalMembers.add(DailyStatResponse.builder().date(weekStart).count(totalCount).build());
            newMembers.add(DailyStatResponse.builder().date(weekStart).count(newMemberCount).build());
            visitors.add(DailyStatResponse.builder().date(weekStart).count(visitorCount).build());
            withdrawals.add(DailyStatResponse.builder().date(weekStart).count(withdrawalCount).build());

            current = current.plusWeeks(1);
        }
    }

    /**
     * 월간 모킹 데이터 생성 (루틴 관리 서비스 패턴)
     */
    private void generateMonthlyMockData(List<DailyStatResponse> totalMembers, List<DailyStatResponse> newMembers,
        List<DailyStatResponse> visitors, List<DailyStatResponse> withdrawals,
        LocalDate startDate, LocalDate endDate) {
        LocalDate current = startDate;
        long totalCount = 4500L;

        while (!current.isAfter(endDate)) {
            // 월의 첫날로 조정
            LocalDate monthStart = current.withDayOfMonth(1);

            // 루틴 앱 특성: 월초 새로운 계획, 연말 건강관리 다짐 등
            int month = current.getMonthValue();
            double monthlyMultiplier = switch (month) {
                case 1 -> 1.4;  // 신년 결심
                case 3 -> 1.2;  // 봄 새 시작
                case 9 -> 1.1;  // 가을 새 시작
                case 12 -> 1.3; // 연말 정리
                default -> 1.0;
            };

            long newMemberCount = (long) ((Math.random() * 80 + 120) * monthlyMultiplier); // 120~199명 * 월별배수
            long visitorCount = (long) ((Math.random() * 400 + 600) * monthlyMultiplier); // 600~999명 * 월별배수
            long withdrawalCount = (long) (Math.random() * 15) + 10; // 10~24명 (꾸준한 이탈)

            totalCount += newMemberCount;

            totalMembers.add(DailyStatResponse.builder().date(monthStart).count(totalCount).build());
            newMembers.add(DailyStatResponse.builder().date(monthStart).count(newMemberCount).build());
            visitors.add(DailyStatResponse.builder().date(monthStart).count(visitorCount).build());
            withdrawals.add(DailyStatResponse.builder().date(monthStart).count(withdrawalCount).build());

            current = current.plusMonths(1);
        }
    }
}
