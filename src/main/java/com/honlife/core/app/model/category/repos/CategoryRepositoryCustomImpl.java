package com.honlife.core.app.model.category.repos;

import static com.honlife.core.app.model.member.domain.QMember.member;

import com.honlife.core.app.model.category.code.CategoryType;
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
    QCategory children = new QCategory("children");

    @Override
    public void softDropByMemberId(Long memberId) {
        queryFactory
            .update(category)
            .set(category.isActive, false)
            .where(category.member.id.eq(memberId))
            .execute();
    }

    @Override
    public List<Category> findDefaultCategory(String userEmail) {

        List<Category> categories = queryFactory
            .select(category).distinct()
            .from(category)
            .leftJoin(category.children, children).fetchJoin()
            .leftJoin(children.member, member).fetchJoin()
            .where(
                category.type.eq(CategoryType.DEFAULT),
                category.isActive.eq(true)
            )
            .fetch();

        // 조건에 맞지 않는 자식 필터링
        for (Category category : categories) {
            category.getChildren().removeIf(child ->
                !userEmail.equals(child.getMember().getEmail()) || !child.getIsActive()
            );
        }

        return categories;
    }

    @Override
    public List<Category> findCustomCategory(String userEmail) {
        List<Category> categories = queryFactory
            .select(category)
            .from(category)
            .leftJoin(category.children, children).fetchJoin()
            .leftJoin(children.member, member).fetchJoin()
            .where(category.type.eq(CategoryType.MAJOR)
                .and(category.member.email.eq(userEmail))
                .and(category.isActive))
            .fetch();

        // 조건에 맞지 않는 자식 필터링
        for (Category category : categories) {
            category.getChildren().removeIf(child ->
                !userEmail.equals(child.getMember().getEmail()) || !child.getIsActive()
            );
        }

        return categories;
    }

}