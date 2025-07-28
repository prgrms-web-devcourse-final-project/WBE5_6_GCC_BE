package com.honlife.core.app.model.badge.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoutineProgressEvent {

    /**
     * 루틴을 처리한 회원 ID
     */
    private Long memberId;

    /**
     * 루틴의 카테고리 ID
     */
    private Long categoryId;

    /**
     * 루틴 완료 여부 (true: 완료, false: 취소)
     */
    private Boolean isDone;
}
