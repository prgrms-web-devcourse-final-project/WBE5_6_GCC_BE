package com.honlife.core.app.model.category.repos;

import java.util.Collection;
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

    Category findByName(String subCategory);
    /**
     * 카테고리 이름과 이메일을 통해 카테고리 조회
     * @param name 카테고리 이름
     * @param memberEmail 멤버 이메일
     * @return Category
     */
    Optional<Category> findCategoryByNameAndMember_Email(String name, String memberEmail);

}
