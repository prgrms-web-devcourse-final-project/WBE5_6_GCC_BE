package com.honlife.core.infra.scheduler;

import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.honlife.core.app.model.routine.repos.RoutineRepository;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

  private final RoutineScheduleRepository routineScheduleRepository;
  private final RoutineRepository routineRepository;

  /** 오늘 날짜 기준으로 자정이 되면 해당 날짜에 해당하는 스케줄러를 추가해줍니다*/
  @Transactional
  @Scheduled(cron = "0 0 0 * * ?")
  public void updateStatus() {

    List<Routine> routines = routineRepository.findAll();

    LocalDate today = LocalDate.now();

    for (Routine routine : routines) {

      /** 루틴에서 오늘날짜에 해당되는 루틴 뭔지 검사한다 */
      if (routine.getRepeatType().isMatched(today, routine.getRepeatValue())) {

        boolean exists = routineScheduleRepository.existsByRoutineAndDate(routine, today);
        if (!exists) {
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
        .date(today)
        .isDone(false)
        .createdAt(LocalDateTime.now())
        .routine(routine)
        .build();

    routineScheduleRepository.save(routineSchedule);


  }
}
