package com.honlife.core.app.controller.admin.dashboard.payload;

import com.honlife.core.app.model.dashboard.dto.DailyStatDTO;
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
public class DailyStatResponse {

    private LocalDate date;

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