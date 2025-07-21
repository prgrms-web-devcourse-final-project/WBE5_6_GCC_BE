package com.honlife.core.app.model.category.repos;

import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.dto.CategoryDTO;
import java.util.Collection;
import java.util.List;

public interface CategoryRepositoryCustom {

    /**
     * 멤버 아이디를 통해 커스텀 카테고리를 소프트 드랍하는 메소드
     * @param memberId 멤버 식별 아이디
     */
    void softDropByMemberId(Long memberId);

    /**
     * 해당 카테고리의 하위 카테고리를 조회합니다.
     * @param userEmail 멤버 이메일
     * @param majorCategory 검색할 카테고리
     * @return {@link CategoryDTO}를 리스트로 반환
     */
    List<Category> findSubCategory(String userEmail, Category majorCategory);

    /**
     * 이메일을 통해 해당 사용자가 만든 카테고리를 검색합니다.
     * @param email 멤버 이메일
     * @param isActive 활성화 여부
     * @return {@link CategoryDTO}를 리스트로 반환
     */
    List<Category> findCategoriesByEmailAndIsActive(String email, boolean isActive);
}
