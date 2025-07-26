package com.honlife.core.app.controller.dashboard.payload;

import lombok.Data;

@Data
public class RoutineTotalCountResponse {
    
    // 그 주 총 루틴 수
    private Long totalCount;
    // 그 주 완료된 루틴 수
    private Long completedCount;
}
