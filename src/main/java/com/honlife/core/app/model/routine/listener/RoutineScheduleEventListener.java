package com.honlife.core.app.model.routine.listener;

import com.honlife.core.app.model.routine.service.RoutineScheduleService;
import com.honlife.core.infra.event.CommonEvent;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RoutineScheduleEventListener {

    private final RoutineScheduleService routineScheduleService;

    @EventListener
    @Transactional
    @Async
    public void createScheduleWhenLogin(CommonEvent event) {

        log.info("handleOnLogin() :: [Routine] Login event handler got event");

        // 루틴 관련 이벤트라면 return
        if(event.getRoutineScheduleId() != null) return;

        routineScheduleService.createRoutineScheduleForMember(event.getMemberEmail(), LocalDate.now());
    }


}
