package com.honlife.core.app.model.badge.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoutineCompletedEvent {

    /**
     * 루틴을 완료한 회원 ID
     */
    private Long memberId;

    /**
     * 완료된 루틴의 카테고리 ID
     */
    private Long categoryId;
}
