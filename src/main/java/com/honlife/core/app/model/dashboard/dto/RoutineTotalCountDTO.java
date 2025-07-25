package com.honlife.core.app.model.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoutineTotalCountDTO {
    
    // 그 주 총 루틴 수
    private Integer totalCount;
    // 그 주 완료된 루틴 수
    private Integer completedCount;
}
