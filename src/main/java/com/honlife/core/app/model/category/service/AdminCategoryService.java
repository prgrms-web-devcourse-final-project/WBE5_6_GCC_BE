package com.honlife.core.app.model.category.service;

import com.honlife.core.app.controller.admin.category.payload.AdminCategoryRequest;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.dto.CategoryDTO;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;
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

    /**
     * 해당하는 기본 카테고리를 참조하는 소분류 카테고리들이 더이상 참조하지 않도록 정리
     * @param categoryId 기본 카테고리 아이디
     */
    @Transactional
    public void removeCategoryReference(Long categoryId) {
        Category deletedCategory = categoryRepository.findByIdAndTypeAndIsActive(categoryId, CategoryType.DEFAULT, true).orElseThrow(()->new CommonException(ResponseCode.NOT_FOUND_CATEGORY));

        // 삭제할 카테고리를 참조하는 모든 SUB 카테고리 조회
        List<Category> categories = categoryRepository.findCategoriesByParent(deletedCategory);

        // 새로운 카테고리를 참조하도록
        categories.forEach(category -> {
            // 삭제할 기본 카테고리를 대신할 사용자 카테고리 생성
            Category newMajorCategory = Category.builder()
                .name(deletedCategory.getName())
                .type(CategoryType.MAJOR)
                .emoji(deletedCategory.getEmoji())
                .member(category.getMember())
                .build();
            categoryRepository.save(newMajorCategory);
            category.setParent(newMajorCategory);
            categoryRepository.save(category);
        });
    }

    /**
     * 기본 카테고리를 소프트 드랍 합니다.
     * @param categoryId 기본 카테고리 아이디
     */
    @Transactional
    public void softDropDefaultCategory(Long categoryId) {

        // 삭제하려는 기본 카테고리를 참조하는 카테고리들 정리
        removeCategoryReference(categoryId);

        Category targetCategory = categoryRepository.findByIdAndTypeAndIsActive(categoryId, CategoryType.DEFAULT, true).orElseThrow(()->new CommonException(ResponseCode.NOT_FOUND_CATEGORY));

        targetCategory.setIsActive(false);
    }
}
