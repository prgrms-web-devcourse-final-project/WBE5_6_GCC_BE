package com.honlife.core.app.controller.category.payload;

import com.honlife.core.app.model.category.code.CategoryType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryPayload {

    private Long id;

    private Long parentId;

    private String name;

    private CategoryType type;

    private Long member;

}
