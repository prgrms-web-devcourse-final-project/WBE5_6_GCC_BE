package com.honlife.core.app.controller.admin.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryCreateRequest {
        @Schema(description = "카테고리 이름", example = "화장실 청소")
        public String categoryName;

        @Schema(description = "카테고리 타입", example = "SUB")
        public CategoryType categoryType;

        @Schema(description = "상위 카테고리 이름", example = "청소")
        public String parentName;
    }