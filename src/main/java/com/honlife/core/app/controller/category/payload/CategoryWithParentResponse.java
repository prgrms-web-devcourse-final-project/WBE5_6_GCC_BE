package com.honlife.core.app.controller.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.dto.CategoryDTO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryWithParentResponse {

    private Long categoryId;

    private String categoryName;

    private CategoryType categoryType;

    private String emoji;

    private Long parent;

    private List<ChildCategoryResponse> children;

}
