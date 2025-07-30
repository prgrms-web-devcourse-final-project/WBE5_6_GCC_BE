package com.honlife.core.app.model.routine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 배지 진행률 업데이트를 위한 루틴 스케줄 정보
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineScheduleInfo {

    /**
     * 루틴을 수행한 회원 ID
     */
    private Long memberId;

    /**
     * 루틴의 카테고리 ID
     */
    private Long categoryId;
}
