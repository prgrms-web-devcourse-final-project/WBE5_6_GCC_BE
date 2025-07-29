package com.honlife.core.app.model.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RoutineTotalCountDTO {
    
    // 그 주 총 루틴 수
    private Long totalCount;
    // 그 주 완료된 루틴 수
    private Long completedCount;
}
