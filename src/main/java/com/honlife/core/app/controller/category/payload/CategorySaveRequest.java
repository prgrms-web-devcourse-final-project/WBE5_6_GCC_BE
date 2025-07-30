package com.honlife.core.app.controller.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategorySaveRequest {

    @Schema(description = "카테고리의 이름", example = "주방 청소")
    @NotBlank
    private String categoryName;

    @Schema(description = "대분류 카테고리인지 소분류 카테고리인지 표현하는 type", example = "SUB")
    @NotNull
    private CategoryType categoryType;

    @Schema(description = "카테고리 이모지", example = "uD83DuDEBD")
    private String emoji;

    @Schema(description = "대분류 카테고리 정보, 만약 type이 MAJOR이라면 null", example = "1")
    private Long parentId;

}
