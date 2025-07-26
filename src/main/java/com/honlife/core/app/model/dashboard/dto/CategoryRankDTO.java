package com.honlife.core.app.model.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRankDTO {

    // 순위
    private Long rank;
    // 카테고리 이름
    private String categoryName;
    // 완료한 카테고리 수
    private Long completedCount;

    public CategoryRankDTO(String categoryName, Long completedCount) {
        this.categoryName = categoryName;
        this.completedCount = completedCount;
    }

}
