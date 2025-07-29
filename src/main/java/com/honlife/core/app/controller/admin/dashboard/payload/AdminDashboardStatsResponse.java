package com.honlife.core.app.controller.admin.dashboard.payload;

import com.honlife.core.app.model.dashboard.dto.AdminDashboardStatsDTO;
import com.honlife.core.app.model.dashboard.dto.DailyStatDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "관리자 대시보드 통계 응답")
public class AdminDashboardStatsResponse {

    @Schema(description = "총 회원 수 (날짜별)")
    private List<DailyStatResponse> totalMembers;

    @Schema(description = "신규 회원 수 (날짜별)")
    private List<DailyStatResponse> newMembers;

    @Schema(description = "방문자 수 (날짜별)")
    private List<DailyStatResponse> visitors;

    @Schema(description = "탈퇴자 수 (날짜별)")
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