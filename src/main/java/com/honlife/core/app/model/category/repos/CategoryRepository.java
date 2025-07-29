package com.honlife.core.app.model.category.repos;

import com.honlife.core.app.model.category.code.CategoryType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.member.domain.Member;


public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {

    /**
     * 해당 멤버와 연관된 첫번째 루틴을 조회
     * @param member
     * @param isActive
     * @return
     */
    Category findFirstByMemberAndIsActive(Member member, Boolean isActive);

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
     * id를 통해 카테고리를 조회합니다.
     * @param id 카테고리 아이디
     * @return Category
     */
    Optional<Category> findCategoryById(Long id);

    /**
     * parnet 카테고리를 통해 해당하는 소분류 카테고리를 조회
     * @param parent 대분류 카테고리
     * @return List<Category>
     */
    List<Category> findCategoriesByParent(Category parent);
}
