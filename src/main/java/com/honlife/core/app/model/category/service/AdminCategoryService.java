package com.honlife.core.app.model.category.service;

import com.honlife.core.app.controller.admin.category.payload.AdminCategoryRequest;
import com.honlife.core.app.model.auth.code.Role;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.dto.CategoryDTO;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;
    private final MemberRepository memberRepository;

    /**
     * 기본 카테고리를 조회합니다.
     * @return {@code List<CategoryDTO>}
     */
    public List<CategoryDTO> findAllDefaultCategory() {
        return categoryRepository.findCategoriesByTypeAndIsActive(CategoryType.DEFAULT, true).stream().map(
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

        Category category = categoryRepository.findByIdAndTypeAndIsActive(categoryId, CategoryType.DEFAULT, true).orElseThrow(
            () -> new CommonException(ResponseCode.NOT_FOUND_CATEGORY)
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

    /**
     * 새로운 기본 카테고리를 생성합니다.
     * @param request 카테고리 정보
     * @param adminEmail 로그인 한 관리자 이메일
     */
    @Transactional
    public void createDefaultCategory(AdminCategoryRequest request, String adminEmail) {
        
        Member admin = memberRepository.findByEmail(adminEmail).orElseThrow(()->new CommonException(ResponseCode.NOT_FOUND_MEMBER));

        Category newDefaultCategory = Category.builder()
            .name(request.getCategoryName())
            .type(CategoryType.DEFAULT)
            .emoji(request.getEmoji())
            .member(admin)
            .build();

        categoryRepository.save(newDefaultCategory);
    }

    /**
     * 기본 카테고리를 수정합니다.
     * @param categoryId 수정할 카테고리
     * @param request 수정할 카테고리 정보
     */
    public void updateDefaultCategory(Long categoryId, AdminCategoryRequest request) {
        Category targetCategory = categoryRepository.findByIdAndTypeAndIsActive(categoryId, CategoryType.DEFAULT, true).orElseThrow(()->new CommonException(ResponseCode.NOT_FOUND_CATEGORY));

        targetCategory.setName(request.getCategoryName());
        targetCategory.setEmoji(request.getEmoji());

        categoryRepository.save(targetCategory);

    }
}
