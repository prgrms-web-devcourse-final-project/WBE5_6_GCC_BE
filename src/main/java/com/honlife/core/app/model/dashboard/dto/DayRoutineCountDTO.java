package com.honlife.core.app.model.dashboard.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayRoutineCountDTO {

    private LocalDateTime date;
    // 그 날 총 루틴 개수
    private Long totalCount;

    // 그 날 완료한 루틴 개수
    private Long completedCount;

    public DayRoutineCountDTO(LocalDate date, Long totalCount, Long completedCount) {
        this.date = date.atStartOfDay();
        this.totalCount = totalCount;
        this.completedCount = completedCount;
    }
}
