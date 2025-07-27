package com.honlife.core.app.model.routine.domain;

import com.honlife.core.app.model.routine.repos.RoutineRepository;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
import com.honlife.core.app.model.routine.service.RoutineScheduleService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

  private final RoutineScheduleRepository routineScheduleRepository;
  private final RoutineRepository routineRepository;
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
