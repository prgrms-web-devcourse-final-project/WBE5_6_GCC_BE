package com.honlife.core.app.model.routine.scheduler;

import com.honlife.core.app.model.routine.service.RoutineScheduleService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

  private final RoutineScheduleService routineScheduleService;

  /** 오늘 날짜 기준으로 자정이 되면 해당 날짜에 해당하는 스케줄러를 추가해줍니다*/
  @Transactional
  @Scheduled(cron = "0 0 0 * * ?")
  public void updateStatus() {
    log.info("[Scheduler] 루틴 스케줄러 실행됨");

    LocalDate today = LocalDate.now();

    routineScheduleService.createSchedule(today);


  }



}
