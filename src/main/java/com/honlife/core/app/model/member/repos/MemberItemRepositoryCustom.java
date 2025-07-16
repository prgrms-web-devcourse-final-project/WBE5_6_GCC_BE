package com.honlife.core.app.model.member.repos;

public interface MemberItemRepositoryCustom {

    /**
     * 멤버 아이디를 통해 멤버 아이템을 소프트 드랍하는 메소드
     * @param memberId 멤버 식별 아이디
     */
    void deleteByMemberId(Long memberId);
}
