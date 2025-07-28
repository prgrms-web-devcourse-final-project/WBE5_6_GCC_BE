package com.honlife.core.app.model.routine.repos;

import com.honlife.core.app.model.member.domain.QMember;
import com.honlife.core.app.model.routine.domain.QRoutine;
import com.honlife.core.app.model.routine.domain.QRoutineSchedule;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoutineScheduleRepositoryCustomImpl implements RoutineScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QRoutineSchedule qRoutineSchedule = QRoutineSchedule.routineSchedule;
    QMember qMember = QMember.member;
    QRoutine qRoutine = QRoutine.routine;

    public Long getCountOfNotCompletedMemberSchedule(LocalDate date, String userEmail) {
        return queryFactory
            .select(qRoutineSchedule.count())
            .from(qRoutineSchedule)
            .leftJoin(qRoutineSchedule.routine, qRoutine)
            .leftJoin(qRoutine.member, qMember)
            .where(
                qRoutineSchedule.scheduleDate.eq(date),
                qMember.email.eq(userEmail),
                qRoutineSchedule.isDone.isFalse()
            )
            .fetchOne();
    }
}
