package com.honlife.core.app.controller.category.payload;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InterestCategoryResponse {

    private Long categoryId;

    private String categoryName;

}
