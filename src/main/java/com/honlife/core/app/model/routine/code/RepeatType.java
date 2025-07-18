package com.honlife.core.app.model.routine.code;

import java.time.LocalDate;

/**
 * 루틴 반복 타입
 */
public enum RepeatType {

    /**
     * 매일 반복
     * repeat_value: null 또는 빈 문자열
     */
    DAILY {
        @Override
        public boolean isMatched(LocalDate date, String value) {
            return true;
        }
    },


    /**
     * 매주 특정 요일 반복
     * repeat_value: "1,3,5" (월,수,금) 형태로 요일 번호 (1=월요일~7=일요일)
     */
    WEEKLY {
        @Override
        public boolean isMatched(LocalDate date, String value) {
            if (value == null || value.isBlank()) return false;
            int dayOfWeek = date.getDayOfWeek().getValue(); // 1~7
            return containsNumber(value, dayOfWeek);
        }
    },

    /**
     * 매월 특정 일 반복
     * repeat_value: "1,15,30" (매월 1일,15일,30일) 형태
     */
    MONTHLY {
        @Override
        public boolean isMatched(LocalDate date, String value) {
            if (value == null || value.isBlank()) return false;
            int dayOfMonth = date.getDayOfMonth(); // 1~31
            return containsNumber(value, dayOfMonth);
        }
    },

    /**
     * 사용자 정의 반복
     * repeat_value: 사용자가 직접 정의한 패턴
     */
    CUSTOM {
        @Override
        public boolean isMatched(LocalDate date, String value) {
            // 아직 사용자 정의 반복은 미지원
            return false;
        }
    };

public abstract boolean isMatched(LocalDate date, String value);

/**string으로 되어있는부분 을 바꿔서 특정 일 있는지 확인**/
protected static boolean containsNumber(String value, int target) {
    for (String part : value.split(",")) {
        if (part.trim().equals(String.valueOf(target))) {
            return true;
        }
    }
    return false;
}
}