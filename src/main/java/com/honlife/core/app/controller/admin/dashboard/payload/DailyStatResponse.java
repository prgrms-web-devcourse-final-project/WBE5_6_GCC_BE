package com.honlife.core.app.controller.admin.dashboard.payload;

import com.honlife.core.app.model.dashboard.dto.DailyStatDTO;
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
@Schema(description = "기간별 통계 응답")
public class DailyStatResponse {

    @Schema(description = "날짜 (일간: 2025-07-28, 주간: 2025-07-21 (주의 첫날), 월간: 2025-07-01 (월의 첫날))", example = "2025-07-28")
    private LocalDate date;

    @Schema(description = "수치", example = "150")
    private Long count;

    /**
     * DTO를 Response로 변환하는 정적 팩토리 메서드
     */
    public static DailyStatResponse fromDto(DailyStatDTO dto) {
        return DailyStatResponse.builder()
            .date(dto.getDate())
            .count(dto.getCount())
            .build();
    }
}