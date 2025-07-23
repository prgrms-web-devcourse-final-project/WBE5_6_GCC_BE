package com.honlife.core.app.model.quest.code;

public enum QuestType {
    /**
     * 연속<br>
     * ex) 루틴 1개 이상 연속 3일 동안 하기
     */
    STREAK,
    /**
     * 하루 기준 연속<br>
     * ex) 3일 연속 ㅇㅇ카테고리 루틴 완료
     */
    DAY_STREAK,
    /**
     * 누적<br>
     * ex) 이번주 ㅇㅇ카테고리 누적 n회
     */
    COUNT,
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
     * 완료<br>
     * ex) 이번 주 하루 루틴 100% 달성
     */
    COMPLETE
}
