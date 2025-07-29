package com.honlife.core.app.model.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "관리자 대시보드 통계 응답 DTO")
public class AdminDashboardStatsDTO {

    @Schema(description = "총 회원 수 (날짜별)")
    private List<DailyStatDTO> totalMembers;

    @Schema(description = "신규 회원 수 (날짜별)")
    private List<DailyStatDTO> newMembers;

    @Schema(description = "방문자 수 (날짜별)")
    private List<DailyStatDTO> visitors;

    @Schema(description = "탈퇴자 수 (날짜별)")
    private List<DailyStatDTO> withdrawals;
}