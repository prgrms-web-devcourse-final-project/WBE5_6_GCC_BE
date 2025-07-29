package com.honlife.core.app.controller.admin.dashboard.payload;

import com.honlife.core.app.model.dashboard.dto.AdminDashboardStatsDTO;
import com.honlife.core.app.model.dashboard.dto.DailyStatDTO;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardStatsResponse {

    private List<DailyStatResponse> totalMembers;

    private List<DailyStatResponse> newMembers;

    private List<DailyStatResponse> visitors;

    private List<DailyStatResponse> withdrawals;

    /**
     * DTO를 Response로 변환하는 정적 팩토리 메서드
     */
    public static AdminDashboardStatsResponse fromDto(AdminDashboardStatsDTO dto) {
        return AdminDashboardStatsResponse.builder()
            .totalMembers(convertDailyStatList(dto.getTotalMembers()))
            .newMembers(convertDailyStatList(dto.getNewMembers()))
            .visitors(convertDailyStatList(dto.getVisitors()))
            .withdrawals(convertDailyStatList(dto.getWithdrawals()))
            .build();
    }

    /**
     * DailyStatDTO List를 DailyStatResponse List로 변환
     */
    private static List<DailyStatResponse> convertDailyStatList(List<DailyStatDTO> dtoList) {
        return dtoList.stream()
            .map(DailyStatResponse::fromDto)
            .collect(Collectors.toList());
    }
}