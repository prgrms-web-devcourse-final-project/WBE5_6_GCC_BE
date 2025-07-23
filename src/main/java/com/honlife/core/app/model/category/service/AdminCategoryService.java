package com.honlife.core.app.model.category.service;

import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.dto.CategoryDTO;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    /**
     * 기본 카테고리를 조회합니다.
     * @return {@code List<CategoryDTO>}
     */
    public List<CategoryDTO> findAllDefaultCategory() {
        return categoryRepository.findCategoriesByType(CategoryType.DEFAULT).stream().map(
            category ->{
                return CategoryDTO.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .emoji(category.getEmoji())
                    .type(category.getType())
                    .createdAt(category.getCreatedAt())
                    .updatedAt(category.getUpdatedAt())
                    .build();
            }).toList();
    }

    /**
     * id를 통해 기본 카테고리를 조회합니다.
     * @param categoryId 카테고리 아이디
     * @return CategoryDTO
     */
    public CategoryDTO findDefaultCategory(Long categoryId) {

        Category category = categoryRepository.findByIdAndType(categoryId,CategoryType.DEFAULT).orElseThrow(
            ()-> new CommonException(ResponseCode.NOT_FOUND_CATEGORY)
        );

        return CategoryDTO.builder()
            .id(category.getId())
            .name(category.getName())
            .emoji(category.getEmoji())
            .type(category.getType())
            .createdAt(category.getCreatedAt())
            .updatedAt(category.getUpdatedAt())
            .build();

    }
}
