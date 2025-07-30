package com.honlife.core.app.model.routine.repos;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RoutineScheduleRepository extends JpaRepository<RoutineSchedule, Long>, RoutineScheduleRepositoryCustom {

    RoutineSchedule findFirstByRoutine(Routine routine);




  List<RoutineSchedule> findByRoutine(Routine routine);


  @Query("SELECT rs FROM RoutineSchedule rs JOIN FETCH rs.routine r JOIN FETCH r.member WHERE rs.id = :id")
  RoutineSchedule findWithRoutineAndMemberById(Long id);


  RoutineSchedule findByRoutineAndScheduledDate(Routine routine, LocalDate now);

  boolean existsByRoutineAndScheduledDate(Routine routine, LocalDate today);

  // 오늘 날짜 기준, 해당 멤버의 완료되지 않은 루틴 스케줄 수
  @Query("""
    SELECT COUNT(rs) 
    FROM RoutineSchedule rs 
    WHERE rs.routine.member.id = :memberId
      AND rs.isDone = false
      AND rs.scheduledDate = :today
""")
  long countTodayIncompleteByMemberId(@Param("memberId") Long memberId,
      @Param("today") LocalDate today);

}
