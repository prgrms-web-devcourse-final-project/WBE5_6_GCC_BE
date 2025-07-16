package com.honlife.core.app.model.category.repos;

import com.honlife.core.app.model.category.domain.QInterestCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InterestCategoryRepositoryCustomImpl implements InterestCategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QInterestCategory interestCategory = QInterestCategory.interestCategory;

    @Override
    public void deleteByMemberId(Long memberId) {
        queryFactory
            .update(interestCategory)
            .set(interestCategory.isActive, false)
            .where(interestCategory.member.id.eq(memberId))
            .execute();
    }
}
