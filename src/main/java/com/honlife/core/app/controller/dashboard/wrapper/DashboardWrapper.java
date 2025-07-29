package com.honlife.core.app.controller.dashboard.wrapper;

import com.honlife.core.app.controller.dashboard.payload.CategoryRankResponse;
import com.honlife.core.app.controller.dashboard.payload.CategoryTotalCountResponse;
import com.honlife.core.app.controller.dashboard.payload.DayRoutineCountResponse;
import com.honlife.core.app.controller.dashboard.payload.RoutineTotalCountResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardWrapper {
    // 한 주 루틴 카운트
    private RoutineTotalCountResponse routineCount;

    // 하루 루틴 카운트
    private List<DayRoutineCountResponse> dayRoutineCount;

    // 한 주 카테고리 카운트
    private List<CategoryTotalCountResponse> categoryCount;

    // 한 주 top 5 카테고리 랭킹
    private List<CategoryRankResponse> top5;

    // 한 주 동안 얻은 누적 포인트량
    private Integer totalPoint;

    // ai 분석 및 조언
    private String aiComment;

}
