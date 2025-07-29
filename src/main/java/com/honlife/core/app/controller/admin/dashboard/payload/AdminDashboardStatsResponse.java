package com.honlife.core.app.controller.admin.dashboard.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
