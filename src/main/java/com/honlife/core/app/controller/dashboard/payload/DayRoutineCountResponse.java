package com.honlife.core.app.controller.dashboard.payload;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * 해당 날짜의 루틴 수를 담은 response
 */
@Data
@Builder
public class DayRoutineCountResponse {

    private LocalDateTime date;

    private Double completionRate;

}
