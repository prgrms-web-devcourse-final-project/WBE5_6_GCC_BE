package com.honlife.core.app.model.category.repos;

import com.honlife.core.app.model.category.domain.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryCustomImpl implements CategoryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    QCategory category = QCategory.category;

    @Override
    public void softDropByMemberId(Long memberId) {
        queryFactory
            .update(category)
            .set(category.isActive, false)
            .where(category.member.id.eq(memberId))
            .execute();
    }
}