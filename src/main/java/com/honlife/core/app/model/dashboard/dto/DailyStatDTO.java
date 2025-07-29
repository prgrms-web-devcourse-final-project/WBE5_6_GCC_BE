package com.honlife.core.app.model.dashboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "기간별 통계 DTO (일/주/월)")
public class DailyStatDTO {

    @Schema(description = "날짜 (일간: 2025-07-28, 주간: 2025-07-21 (주의 첫날), 월간: 2025-07-01 (월의 첫날))", example = "2025-07-28")
    private LocalDate date;

    @Schema(description = "수치", example = "150")
    private Long count;
}
