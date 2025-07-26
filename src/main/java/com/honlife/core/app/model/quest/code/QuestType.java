package com.honlife.core.app.model.quest.code;

public enum QuestType {
    /**
     * 루틴 누적<br>
     * ex) 이번주 아무 루틴 n회 이상
     */
    ROUTINE_COUNT,
    /**
     * 누적<br>
     * ex) 이번주 ㅇㅇ카테고리 누적 n회
     */
    CATEGORY_COUNT,
    /**
     * 완료 누적<br>
     * ex) 이번주 루틴 100% 달성 n회
     */
    COMPLETE_COUNT,
    /**
     * 완료 연속<br>
     * ex) 이번주 n일 연속 100% 달성
     */
    COMPLETE_STREAK,
    /**
     * 누적 로그인
     * ex) 이벤트 기간동안 누적 n회 로그인
     */
    LOGIN_COUNT,
    /**
     * 연속 로그인 ex) 이벤트 기간동안 연속 로그인
     */
    LOGIN_STREAK
}
