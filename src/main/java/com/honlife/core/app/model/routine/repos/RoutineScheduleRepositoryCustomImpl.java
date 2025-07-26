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

@Repository
@RequiredArgsConstructor
public class RoutineScheduleRepositoryCustomImpl implements RoutineScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QRoutineSchedule routineSchedule=QRoutineSchedule.routineSchedule;
    private final QRoutine routine=QRoutine.routine;
    private final QCategory category=QCategory.category;

    @Override
    public RoutineTotalCountDTO countRoutineScheduleByMemberAndDateAndIsDone(String userEmail, LocalDate startDate, LocalDate endDate) {
        return queryFactory
            .select(Projections.constructor(RoutineTotalCountDTO.class,
                    routineSchedule.count(),
                    JPAExpressions
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
    public List<DayRoutineCountDTO> countRoutineSchedulesGroupByDate(String userEmail, LocalDate startDate, LocalDate endDate) {
        QRoutineSchedule outerRoutineSchedule = new QRoutineSchedule("outerRoutineSchedule");

         return queryFactory
            .select(Projections.constructor(DayRoutineCountDTO.class,
                outerRoutineSchedule.date,
                outerRoutineSchedule.count(),
                JPAExpressions.select(routineSchedule.count())
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
                parent.name,
                category.name,
                routineSchedule.count()
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
