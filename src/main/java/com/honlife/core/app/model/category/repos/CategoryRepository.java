package com.honlife.core.app.model.category.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.member.domain.Member;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findFirstByMember(Member member);

}
