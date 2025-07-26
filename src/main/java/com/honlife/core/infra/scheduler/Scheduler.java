package com.honlife.core.infra.scheduler;

import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.honlife.core.app.model.routine.repos.RoutineRepository;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
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

  /** 오늘 날짜 기준으로 자정이 되면 해당 날짜에 해당하는 스케줄러를 추가해줍니다*/
  @Transactional
  @Scheduled(cron = "0 0 0 * * ?")
  public void updateStatus() {
    log.info("[Scheduler] 루틴 스케줄러 실행됨");

    List<Routine> routines = routineRepository.findAllByIsActiveTrue();


    LocalDate today = LocalDate.now();

    for (Routine routine : routines) {

      /** 루틴에서 오늘날짜에 해당되는 루틴 뭔지 검사한다
       * 그리고 startRoutineDate를 이용해서 루틴 시작 날짜부터 계산해서 스케줄을 추가해준다
       * 또한 주기를 받는거에서 주기에 해당한는 주만 반영이 되도록 추가해준다*/
      if (!today.isBefore(routine.getStartRoutineDate()) &&
          routine.getRepeatType().isMatched(today, routine.getRepeatValue()) &&
          ChronoUnit.WEEKS.between(routine.getStartRoutineDate(), today) % routine.getRepeatInterval() == 0
      ) {

        boolean exists = routineScheduleRepository.existsByRoutineAndScheduleDate(routine, today);
          if(!exists){

            log.info("✅ [Scheduler] 루틴 ID {} 에 대한 스케줄 생성", routine.getId());
            createSchedule(routine.getId(), today);

          }

        }

      }

  }


  /** DB에 해당 루틴에 관련된 거 DB에 저장하는 로직 구현*/
  @Transactional
  public void createSchedule(Long routineId, LocalDate today){

    Routine routine = routineRepository.findById(routineId)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_ROUTINE));

    RoutineSchedule routineSchedule = RoutineSchedule.builder()
        .scheduleDate(today)
        .isDone(false)
        .routine(routine)
        .build();

    routineSchedule.setCreatedAt(LocalDateTime.now());
    routineSchedule.setUpdatedAt(LocalDateTime.now());


    routineScheduleRepository.save(routineSchedule);
    log.info("[Scheduler] 루틴 ID {} 의 {} 스케줄 저장 완료", routineId, today);



  }
}
