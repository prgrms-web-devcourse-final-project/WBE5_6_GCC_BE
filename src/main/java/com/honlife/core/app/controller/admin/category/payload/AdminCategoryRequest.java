package com.honlife.core.app.controller.admin.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminCategoryRequest {

    @NotBlank
    public String categoryName;

    public String emoji;
}