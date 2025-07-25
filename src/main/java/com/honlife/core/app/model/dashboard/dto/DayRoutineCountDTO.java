package com.honlife.core.app.model.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DayRoutineCountDTO {

    // 그 날 총 루틴 개수
    private Integer totalCount;

    // 그 날 완료한 루틴 개수
    private Integer completedCount;
}
