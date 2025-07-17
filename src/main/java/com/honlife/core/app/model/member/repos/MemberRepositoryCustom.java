package com.honlife.core.app.model.member.repos;

public interface MemberRepositoryCustom {

    /**
     * 이메일 인증이 완료된 상태의 계정인지 확인합니다.
     * @param email 검증할 이메일
     * @return {@code Boolean}
     */
    boolean isEmailVerified(String email);

    /**
     * 멤버 소프트 드랍
     * @param userEmail 유저 이메일
     */
    void softDropMember(String userEmail);
}
