package com.honlife.core.app.controller.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategorySaveRequest {

    @NotBlank
    private String categoryName;

    @NotNull
    private CategoryType categoryType;

    private String parentName;

    private String emoji;

}
