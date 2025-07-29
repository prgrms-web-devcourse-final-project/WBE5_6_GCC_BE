package com.honlife.core.app.model.category.repos;

import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.dto.CategoryDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.member.domain.Member;


public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

    Category findFirstByMember(Member member);

    /**
     * 해당 멤버와 연관된 첫번째 루틴을 조회
     * @param member
     * @param isActive
     * @return
     */
    Category findFirstByMemberAndIsActive(Member member, Boolean isActive);
    /**
     * 카테고리 이름과 이메일을 통해 카테고리 조회
     * @param name 카테고리 이름
     * @param memberEmail 멤버 이메일
     * @return Category
     */
    Optional<Category> findCategoryByNameAndMember_Email(String name, String memberEmail);

    /**
     * 타입을 통해 카테고리를 조회
     * @param type 카테고리 타입
     * @return List<Category>
     */
    List<Category> findCategoriesByTypeAndIsActive(CategoryType type, Boolean isActive);

    /**
     * id와 타입을 통해 카테고리를 조회
     * @param id 카테고리 id
     * @param type 타입
     * @return Optional<Category>
     */
    Optional<Category> findByIdAndTypeAndIsActive(Long id, CategoryType type, Boolean isActive);
    /**
     * 활성화된 카테고리 중 해당하는 이름을 가진 카테고리가 있는지 확인합니다. (회원이 가지고 있는 카테고리 내에서 비교)
     * @param name 카테고리 이름
     * @param isActive 활성화 여부
     * @param memberEmail 멤버 이메일
     * @return Boolean
     */
    boolean existsCategoriesByNameAndIsActiveAndMember_Email(String name, Boolean isActive, String memberEmail);

    /**
     * 선택한 타입의 카테고리와 이름이 겹치는 카테고리가 있는지 확인합니다.
     * @param type 카테고리 타입
     * @param name 카테고리 이름
     * @return Boolean
     */
    boolean existsCategoryByTypeAndName(CategoryType type, String name);
}
