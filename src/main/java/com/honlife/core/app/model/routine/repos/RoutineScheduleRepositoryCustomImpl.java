package com.honlife.core.app.model.routine.repos;

import com.honlife.core.app.model.category.domain.QCategory;
import com.honlife.core.app.model.dashboard.dto.CategoryRankDTO;
import com.honlife.core.app.model.dashboard.dto.CategoryCountDTO;
import com.honlife.core.app.model.dashboard.dto.DayRoutineCountDTO;
import com.honlife.core.app.model.dashboard.dto.RoutineTotalCountDTO;
import com.honlife.core.app.model.routine.domain.QRoutine;
import com.honlife.core.app.model.routine.domain.QRoutineSchedule;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.honlife.core.app.model.member.domain.QMember;
import com.honlife.core.app.model.routine.domain.QRoutineSchedule;
import com.honlife.core.app.model.routine.domain.QRoutine;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RoutineScheduleRepositoryCustomImpl implements RoutineScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QRoutineSchedule routineSchedule=QRoutineSchedule.routineSchedule;
    private final QRoutineSchedule qRoutineSchedule=QRoutineSchedule.routineSchedule;
    private final QRoutine routine=QRoutine.routine;
    private final QRoutine qRoutine=QRoutine.routine;
    QMember qMember = QMember.member;
    private final QCategory category=QCategory.category;

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
    @Override
    public RoutineTotalCountDTO countRoutineScheduleByMemberAndDateBetweenAndIsDone(String userEmail, LocalDate startDate, LocalDate endDate) {
        return queryFactory
            .select(Projections.constructor(RoutineTotalCountDTO.class,
                    routineSchedule.count(), // 총 루틴 수
                    JPAExpressions // 완료한 루틴 수
                        .select(routineSchedule.count())
                        .from(routineSchedule)
                        .where((routineSchedule.date.goe(startDate))
                            .and(routineSchedule.date.lt(endDate))
                            .and(routineSchedule.isDone)
                            .and(routineSchedule.routine.member.email.eq(userEmail))
                )))
            .from(routineSchedule)
            .where((routine.member.email.eq(userEmail))
                .and(routineSchedule.date.goe(startDate))
                .and(routineSchedule.date.lt(endDate))
            )
            .fetchOne();
    }

    @Override
    public List<DayRoutineCountDTO> countRoutineSchedulesGroupByDateBetween(String userEmail, LocalDate startDate, LocalDate endDate) {
        QRoutineSchedule outerRoutineSchedule = new QRoutineSchedule("outerRoutineSchedule");

         return queryFactory
            .select(Projections.constructor(DayRoutineCountDTO.class,
                outerRoutineSchedule.date, // 해당 날짜
                outerRoutineSchedule.count(), // 해당 날짜의 총 루틴 수
                JPAExpressions.select(routineSchedule.count()) // 해당 날짜의 완료한 루틴 수
                    .from(routineSchedule)
                    .where((routineSchedule.date.eq(outerRoutineSchedule.date))
                        .and(routineSchedule.routine.member.email.eq(userEmail)
                        .and(routineSchedule.isDone))
                    )
                )
            )
            .from(outerRoutineSchedule)
            .where((outerRoutineSchedule.routine.member.email.eq(userEmail))
                .and(outerRoutineSchedule.date.goe(startDate))
                .and(outerRoutineSchedule.date.lt(endDate))
            )
             .groupBy(outerRoutineSchedule.date)
             .fetch();
    }

    @Override
    public List<CategoryCountDTO> countRoutineSchedulesGroupByCategory(String userEmail, LocalDate startDate, LocalDate endDate, Boolean isDone) {
        QCategory parent= new QCategory("parent");

        return queryFactory
            .select(Projections.constructor(CategoryCountDTO.class,
                parent.name, // 부모 카테고리의 이름
                category.name, // 루틴에 저장된 카테고리의 이름
                routineSchedule.count() // 카테고리 별 루틴 수
            ))
            .from(routineSchedule)
            .leftJoin(routineSchedule.routine.category, category)
            .leftJoin(category.parent, parent)
            .where(routine.member.email.eq(userEmail)
                .and(routineSchedule.date.goe(startDate))
                .and(routineSchedule.date.lt(endDate))
                .and(isDone != null ? routineSchedule.isDone.eq(isDone) : null))
            .groupBy(category, parent)
            .orderBy(routineSchedule.count().desc())
            .fetch();
    }
}