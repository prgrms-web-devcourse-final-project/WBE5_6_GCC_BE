package com.honlife.core.app.controller.admin.dashboard.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
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
@Schema(description = "기간별 통계 응답")
public class DailyStatResponse {

    @Schema(description = "날짜 (일간: 2025-07-28, 주간: 2025-07-21 (주의 첫날), 월간: 2025-07-01 (월의 첫날))", example = "2025-07-28")
    private LocalDate date;

    @Schema(description = "수치", example = "150")
    private Long count;

}
