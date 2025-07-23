package com.honlife.core.app.controller.admin.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.dto.CategoryDTO;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminCategoryResponse {

    private Long categoryId;
    private String categoryName;
    private CategoryType categoryType;
    private String emoji;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    public static AdminCategoryResponse fromDTO(CategoryDTO categoryDTO) {
        return AdminCategoryResponse.builder()
            .categoryId(categoryDTO.getId())
            .categoryName(categoryDTO.getName())
            .categoryType(categoryDTO.getType())
            .emoji(categoryDTO.getEmoji())
            .createTime(categoryDTO.getCreatedAt())
            .updateTime(categoryDTO.getUpdatedAt())
            .build();
    }
}
