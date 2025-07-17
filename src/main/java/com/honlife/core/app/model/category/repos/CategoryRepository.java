package com.honlife.core.app.model.category.repos;

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
}
