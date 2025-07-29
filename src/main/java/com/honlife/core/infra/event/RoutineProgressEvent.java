package com.honlife.core.infra.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 배지 진행률 업데이트를 위한 루틴 완료/취소 이벤트
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutineProgressEvent {

    /**
        * 루틴 스케줄 ID
     */
    private Long routineScheduleId;

    /**
     * 루틴 완료 여부 (true: 완료, false: 취소)
     */
    private Boolean isDone;
}
