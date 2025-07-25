package com.honlife.core.app.controller.dashboard.payload;

import lombok.Data;

@Data
public class CategoryRankResponse {

    // 순위
    private Integer rank;
    // 카테고리 이름
    private String categoryName;
    // 완료한 카테고리 수
    private Integer completedCount;

}
