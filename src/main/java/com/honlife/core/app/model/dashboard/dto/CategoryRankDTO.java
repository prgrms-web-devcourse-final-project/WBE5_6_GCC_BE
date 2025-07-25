package com.honlife.core.app.model.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryRankDTO {

    // 순위
    private Integer rank;
    // 카테고리 이름
    private String categoryName;
    // 완료한 카테고리 수
    private Integer completedCount;

}
