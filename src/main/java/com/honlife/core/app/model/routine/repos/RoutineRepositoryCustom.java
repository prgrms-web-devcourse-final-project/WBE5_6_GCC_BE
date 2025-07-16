package com.honlife.core.app.model.routine.repos;

public interface RoutineRepositoryCustom {

    /**
     * 멤버 아이디를 통해 루틴을 소프트 드랍하는 메소드
     * @param memberId 멤버 식별 아이디
     */
    void deleteByMemberId(Long memberId);
}
