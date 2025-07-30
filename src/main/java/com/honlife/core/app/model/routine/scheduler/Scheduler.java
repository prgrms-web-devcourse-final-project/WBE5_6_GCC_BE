package com.honlife.core.app.model.routine.scheduler;

import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.notification.service.NotifyListService;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
import com.honlife.core.app.model.routine.service.RoutineScheduleService;
import java.time.LocalDate;
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

  private final RoutineScheduleService routineScheduleService;
  private final MemberRepository memberRepository;
  private final RoutineScheduleRepository routineScheduleRepository;
  private final NotifyListService notifyService;

  /** 오늘 날짜 기준으로 자정이 되면 해당 날짜에 해당하는 스케줄러를 추가해줍니다*/
  @Transactional
  @Scheduled(cron = "0 0 0 * * ?")
  public void updateStatus() {
    log.info("[Scheduler] 루틴 스케줄러 실행됨");

    LocalDate today = LocalDate.now();

    routineScheduleService.createSchedule(today);

  }
  @Scheduled(cron = "0 0 9,15,21 * * ?")// 오전 9시, 오후 3시, 밤 9시
  public void checkAndNotifyIncompleteRoutines() {

    log.info("🔔 [Scheduler] 미완료 루틴 알림 스케줄러 실행됨");
    List<Member> members = memberRepository.findAll();

    for (Member member : members) {
      long count = routineScheduleRepository.countTodayIncompleteByMemberId(member.getId(), LocalDate.now());

      log.info("👤 Member ID {} - 미완료 루틴 {}개", member.getId(), count);

      if (count > 0) {
        notifyService.notifyIncompleteRoutines(member, count);
      }
    }
  }



}
