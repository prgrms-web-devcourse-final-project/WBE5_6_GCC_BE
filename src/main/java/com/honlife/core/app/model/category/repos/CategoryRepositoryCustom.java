package com.honlife.core.app.model.category.repos;

import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.dto.CategoryDTO;
import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryCustom {

    /**
     * 멤버 아이디를 통해 커스텀 카테고리를 소프트 드랍하는 메소드
     * @param memberId 멤버 식별 아이디
     */
    void softDropByMemberId(Long memberId);

    /**
     * 기본 카테고리와 함께 사용자가 생성한 하위 카테고리를 함께 반환합니다.
     * @param userEmail 멤버 이메일
     * @return {@link CategoryDTO}를 리스트로 반환
     */
    List<Category> findDefaultCategory(String userEmail);

    /**
     * 사용자가 생성한 상위 카테고리와 함께 사용자가 생성한 하위 카테고리를 반환합니다.
     * @param userEmail 멤버 이메일
     * @return {@code List<Category>}
     */
    List<Category> findCustomCategory(String userEmail);

    /**
     * 카테고리 아이디와 이메일을 통해 카테고리를 검색합니다.
     * @param categoryId 카테고리 아이디
     * @param userEmail 멤버 이메일
     * @return {@link CategoryDTO}를 Optional로 반환
     */
    Optional<Category> findCategoryById(Long categoryId, String userEmail);
}
