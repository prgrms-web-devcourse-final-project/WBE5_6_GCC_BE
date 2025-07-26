package com.honlife.core.app.controller.dashboard.payload;

import lombok.Data;

@Data
public class CategoryTotalCountResponse {
    // 카테고리 이름
    private String categoryName;
    // 카테고리와 관련된 루틴이 생성된 횟수
    private Long totalCount;

}
