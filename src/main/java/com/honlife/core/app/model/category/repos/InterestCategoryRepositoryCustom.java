package com.honlife.core.app.model.category.repos;

public interface InterestCategoryRepositoryCustom {

    /**
     * 멤버 아이디를 통해 선호 카테고리를 소프트 드랍하는 메소드
     * @param memberId 멤버 식별 아이디
     */
    void deleteByMemberId(Long memberId);
}
