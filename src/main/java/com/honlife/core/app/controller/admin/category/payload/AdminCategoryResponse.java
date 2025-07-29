package com.honlife.core.app.controller.admin.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminCategoryResponse {

    @Schema(example = "1")
    private Long categoryId;
    @Schema(example = "청소")
    private String categoryName;
    @Schema(example = "DEFAULT")
    private CategoryType categoryType;
    @Schema(example = "🧹")
    private String emoji;
    @Schema(example = "2025-07-09T21:30:00")
    private LocalDateTime createTime;
    @Schema(example = "2025-07-09T21:30:00")
    private LocalDateTime updateTime;
}
