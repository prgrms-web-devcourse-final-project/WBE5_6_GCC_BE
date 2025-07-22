package com.honlife.core.app.controller.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.dto.ChildCategoryDTO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChildCategoryPayload {

    private Long categoryId;

    private Long parentId;

    private String categoryName;

    private CategoryType categoryType;

    private String emoji;

    public static List<ChildCategoryPayload> fromDTO(List<ChildCategoryDTO> children) {

        return children.stream().map(
            child -> {
                return ChildCategoryPayload.builder()
                    .categoryId(child.getCategoryId())
                    .categoryName(child.getCategoryName())
                    .categoryType(child.getCategoryType())
                    .emoji(child.getEmoji())
                    .build();
            }).toList();
    }
}
