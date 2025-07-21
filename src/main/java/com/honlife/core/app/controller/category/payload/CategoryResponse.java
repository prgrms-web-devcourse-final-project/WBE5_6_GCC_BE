package com.honlife.core.app.controller.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.dto.CategoryUserViewDTO;
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

    private String emoji;

    private Long memberId;


    public static CategoryResponse fromDTO(CategoryUserViewDTO categoryDTO) {
        return CategoryResponse.builder()
            .categoryId(categoryDTO.getId())
            .categoryName(categoryDTO.getName())
            .categoryType(categoryDTO.getType())
            .emoji(categoryDTO.getEmoji())
            .parentId(categoryDTO.getParentId())
            .parentName(categoryDTO.getParentName())
            .memberId(categoryDTO.getMember())
            .build();
    }

}
