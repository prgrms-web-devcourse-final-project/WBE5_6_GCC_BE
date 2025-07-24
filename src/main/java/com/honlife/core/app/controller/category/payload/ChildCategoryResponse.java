package com.honlife.core.app.controller.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChildCategoryResponse {

    private Long categoryId;

    private Long parentId;

    private String categoryName;

    private CategoryType categoryType;

    private String emoji;
}
