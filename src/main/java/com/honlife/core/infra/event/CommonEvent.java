package com.honlife.core.infra.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

// TODO: Routine 완료 API merge 되면 Service에 이벤트 호출 넣기
/**
 * 루틴 완료시 퀘스트 진행상황 업데이트를 위한 이벤트 클래스
 */
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CommonEvent {

    private final String memberEmail;
    private Long routineScheduleId;
    private Long routineId;
    private Boolean isDone;
}
