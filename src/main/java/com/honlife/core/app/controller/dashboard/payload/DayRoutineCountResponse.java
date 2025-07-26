package com.honlife.core.app.controller.dashboard.payload;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DayRoutineCountResponse {

    private LocalDateTime date;
    // 그 날 총 루틴 개수
    private Long totalCount;

    // 그 날 완료한 루틴 개수
    private Long completedCount;
}
