package com.honlife.core.app.controller.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryResponse {

    private Long categoryId;

    private Long parentId;

    private String parentName;

    private String categoryName;

    private CategoryType categoryType;

    private Long memberId;

}
