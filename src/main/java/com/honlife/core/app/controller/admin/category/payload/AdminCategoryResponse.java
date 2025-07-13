package com.honlife.core.app.controller.admin.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminCategoryResponse {

    private Long categoryId;
    private String categoryName;
    private CategoryType categoryType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
