package com.honlife.core.app.controller.category.wrapper;

import com.honlife.core.app.controller.category.payload.CategoryResponse;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CategoryWrapper {

    private final List<CategoryResponse> categories;
}
