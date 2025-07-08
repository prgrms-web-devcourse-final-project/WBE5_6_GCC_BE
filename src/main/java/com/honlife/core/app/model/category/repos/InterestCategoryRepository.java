package com.honlife.core.app.model.category.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.domain.InterestCategory;
import com.honlife.core.app.model.member.domain.Member;


public interface InterestCategoryRepository extends JpaRepository<InterestCategory, Long> {

    InterestCategory findFirstByCategory(Category category);

    InterestCategory findFirstByMember(Member member);

}
