package com.honlife.core.app.controller.admin.dashboard;

import com.honlife.core.app.model.dashboard.code.PeriodType;
import com.honlife.core.app.model.dashboard.dto.AdminDashboardStatsDTO;
import com.honlife.core.app.model.dashboard.service.AdminDashboardService;
import com.honlife.core.infra.response.CommonApiResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonApiResponse<AdminDashboardStatsDTO> getDashboardStats(
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
        @RequestParam(defaultValue = "DAILY") PeriodType periodType
    ) {
        AdminDashboardStatsDTO stats = adminDashboardService.getDashboardStats(startDate, endDate, periodType);
        return CommonApiResponse.success(stats);
    }
}
