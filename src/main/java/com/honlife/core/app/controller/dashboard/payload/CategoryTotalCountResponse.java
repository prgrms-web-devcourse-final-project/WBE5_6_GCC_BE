package com.honlife.core.app.controller.dashboard.payload;

import lombok.Builder;
import lombok.Data;

/**
 * 카테고리 이름과 해당 카테고리가 쓰인 루틴이 등록된 횟수를 담은 response
 * 카테고리 점유율에 사용
 */
@Data
@Builder
public class CategoryTotalCountResponse {
    // 카테고리 이름
    private String categoryName;
    // 카테고리와 관련된 루틴이 생성된 횟수
    private Long totalCount;

}
