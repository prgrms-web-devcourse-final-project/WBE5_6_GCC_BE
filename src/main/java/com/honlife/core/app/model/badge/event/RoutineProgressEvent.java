package com.honlife.core.app.model.badge.event;

import com.honlife.core.app.model.badge.code.ProgressAction;
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
     * 진행률 변경 액션 (COMPLETED: +1, CANCELLED: -1)
     */
    private ProgressAction action;
}
