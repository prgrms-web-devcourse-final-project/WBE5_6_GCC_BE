package com.honlife.core.app.model.dashboard.code;

/**
 * 통계 집계 기간 타입
 */
public enum PeriodType {
    DAILY("일간"),
    WEEKLY("주간"),
    MONTHLY("월간");

    private final String description;

    PeriodType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
