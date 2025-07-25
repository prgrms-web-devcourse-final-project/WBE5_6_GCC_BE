package com.honlife.core.app.controller.dashboard.warpper;

import com.honlife.core.app.controller.dashboard.payload.CategoryRankResponse;
import com.honlife.core.app.controller.dashboard.payload.CategoryTotalCountResponse;
import com.honlife.core.app.controller.dashboard.payload.DailyRoutineCountResponse;
import com.honlife.core.app.controller.dashboard.payload.RoutineTotalCountResponse;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDashboardWrapper {
    // 한 주 루틴 카운트
    private RoutineTotalCountResponse routineCount;

    // 하루 루틴 카운트
    private Map<LocalDateTime, DailyRoutineCountResponse> dailyRoutineCount;

    // 한 주 카테고리 카운트
    private CategoryTotalCountResponse categoryCount;

    // 한 주 top 5 카테고리 랭킹
    private CategoryRankResponse[] top5 = new CategoryRankResponse[5];

    // 한 주 동안 얻은 누적 포인트량
    private Integer totalPoint;

    // ai 분석 및 조언
    private String aiComment;

}
