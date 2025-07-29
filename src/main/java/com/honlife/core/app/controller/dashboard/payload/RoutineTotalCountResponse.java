package com.honlife.core.app.controller.dashboard.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 해당 주의 총 루틴 수를 담은 Response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineTotalCountResponse {
    
    // 그 주 총 루틴 수
    private Long totalCount;
    // 그 주 완료된 루틴 수
    private Long completedCount;
}
