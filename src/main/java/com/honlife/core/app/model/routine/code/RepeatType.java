package com.honlife.core.app.model.routine.code;

/**
 * 루틴 반복 타입
 */
public enum RepeatType {

    /**
     * 매일 반복
     * repeat_value: null 또는 빈 문자열
     */
    DAILY,

    /**
     * 매주 특정 요일 반복
     * repeat_value: "1,3,5" (월,수,금) 형태로 요일 번호 (1=월요일~7=일요일)
     */
    WEEKLY,

    /**
     * 매월 특정 일 반복
     * repeat_value: "1,15,30" (매월 1일,15일,30일) 형태
     */
    MONTHLY,

    /**
     * 사용자 정의 반복
     * repeat_value: 사용자가 직접 정의한 패턴
     */
    CUSTOM,

    /**
     * 반복 없음 (일회성)
     * repeat_value: null 또는 빈 문자열
     */
    NONE
}
