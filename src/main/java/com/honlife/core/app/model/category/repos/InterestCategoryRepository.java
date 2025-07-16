package com.honlife.core.app.model.category.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.domain.InterestCategory;
import com.honlife.core.app.model.member.domain.Member;


public interface InterestCategoryRepository extends JpaRepository<InterestCategory, Long> {

    InterestCategory findFirstByCategory(Category category);

    InterestCategory findFirstByMember(Member member);

    /**
     * 회원의 관심카테고리 데이터를 전부 불러옴(비활성화 포함)
     * @param member 회원
     * @return {@code List<}{@link InterestCategory}{@code >}
     */
    List<InterestCategory> findAllByMember(Member member);
}
