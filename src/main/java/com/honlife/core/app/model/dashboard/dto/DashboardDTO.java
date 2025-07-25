package com.honlife.core.app.model.dashboard.dto;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {
    // 한 주 루틴 카운트
    private RoutineTotalCountDTO routineCount;

    // 하루 루틴 카운트
    private Map<LocalDateTime, DayRoutineCountDTO> dayRoutineCount;

    // 한 주 카테고리 카운트
    private CategoryTotalCountDTO categoryCount;

    // 한 주 top 5 카테고리 랭킹
    private CategoryRankDTO[] top5 = new CategoryRankDTO[5];

    // 한 주 동안 얻은 누적 포인트량
    private Integer totalPoint;

    // ai 분석 및 조언
    private String aiComment;

}
