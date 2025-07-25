package com.honlife.core.app.model.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryTotalCountDTO {
    // 카테고리 이름
    private String categoryName;
    // 카테고리와 관련된 루틴이 생성된 횟수
    private Integer totalCount;

}
