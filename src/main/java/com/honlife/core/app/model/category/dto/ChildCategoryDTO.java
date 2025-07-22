package com.honlife.core.app.model.category.dto;

import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.domain.Category;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChildCategoryDTO {

    private Long categoryId;

    private Long parentId;

    private String categoryName;

    private CategoryType categoryType;

    private String emoji;

    public static ChildCategoryDTO fromEntity(Category category) {
        return ChildCategoryDTO.builder()
            .categoryId(category.getId())
            .categoryName(category.getName())
            .categoryType(category.getType())
            .emoji(category.getEmoji())
            .parentId(category.getParent().getId())
            .build();
    }

}
