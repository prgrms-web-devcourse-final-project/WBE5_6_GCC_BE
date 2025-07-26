package com.honlife.core.app.controller.dashboard.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 순위와 카테고리 네임, 카테고리를 참조한 완료한 루틴 수를 담은 response
 * top5 순위에 사용
 */
@Data
public class CategoryRankResponse {

    // 순위
    private Long rank;
    // 카테고리 이름
    private String categoryName;
    // 완료한 카테고리 수
    private Long completedCount;

}
