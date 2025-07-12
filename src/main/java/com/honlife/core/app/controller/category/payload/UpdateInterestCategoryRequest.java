package com.honlife.core.app.controller.category.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateInterestCategoryRequest {

    @NotEmpty
    @Schema(description = "회원이 관심 있는 카테고리 ID 목록", example = "[1, 3, 5]")
    private List<Long> interestedCategoryIds;

}
