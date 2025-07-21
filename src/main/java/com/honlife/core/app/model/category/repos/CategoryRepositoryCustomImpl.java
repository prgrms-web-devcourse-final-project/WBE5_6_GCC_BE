package com.honlife.core.app.model.category.repos;

import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.domain.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
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

    @Override
    public List<Category> findSubCategory(String userEmail, Category majorCategory) {
        return queryFactory
            .select(category)
            .from(category)
            .leftJoin(category.parent).fetchJoin()
            .where(category.parent.eq(majorCategory).and(category.member.email.eq(userEmail)).and(category.isActive))
            .fetch();
    }

    @Override
    public List<Category> findCategoriesByEmailAndIsActive(String email, boolean isActive) {
        return queryFactory
            .select(category)
            .from(category)
            .leftJoin(category.parent).fetchJoin()
            .where((category.member.email.eq(email)).and(category.isActive.eq(isActive)))
            .fetch();
    }

    @Override
    public Optional<Category> findCategoryById(Long categoryId) {
        return Optional.ofNullable(
            queryFactory
            .select(category)
            .from(category)
            .leftJoin(category.parent).fetchJoin()
            .where(category.id.eq(categoryId))
            .fetchOne());
    }
}