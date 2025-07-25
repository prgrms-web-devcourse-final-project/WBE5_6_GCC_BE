package com.honlife.core.app.controller.dashboard.payload;

import lombok.Data;

@Data
public class DayRoutineCountResponse {

    // 그 날 총 루틴 개수
    private Integer totalCount;

    // 그 날 완료한 루틴 개수
    private Integer completedCount;
}
