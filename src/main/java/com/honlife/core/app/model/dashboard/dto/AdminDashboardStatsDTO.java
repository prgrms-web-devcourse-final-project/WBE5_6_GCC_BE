package com.honlife.core.app.model.dashboard.dto;

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
public class AdminDashboardStatsDTO {

    private List<DailyStatDTO> totalMembers;

    private List<DailyStatDTO> newMembers;

    private List<DailyStatDTO> visitors;

    private List<DailyStatDTO> withdrawals;
}