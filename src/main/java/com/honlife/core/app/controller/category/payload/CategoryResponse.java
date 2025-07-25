package com.honlife.core.app.controller.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.dto.CategoryDTO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryResponse {

    private Long categoryId;

    private String categoryName;

    private CategoryType categoryType;

    private String emoji;

    private List<ChildCategoryResponse> children;


    public static CategoryResponse fromDTO(CategoryDTO categoryDTO) {
        return CategoryResponse.builder()
            .categoryId(categoryDTO.getId())
            .categoryName(categoryDTO.getName())
            .categoryType(categoryDTO.getType())
            .emoji(categoryDTO.getEmoji())
            .children(ChildCategoryResponse.fromDTO(categoryDTO.getChildren()))
            .build();
    }

}
