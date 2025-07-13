package com.honlife.core.app.controller.admin.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminCategoryRequest {

    @NotBlank
    @Schema(description = "카테고리 이름", example = "청소")
    public String categoryName;

    @Schema(description = "카테고리 타입", example = "MAJOR")
    public CategoryType categoryType;
}