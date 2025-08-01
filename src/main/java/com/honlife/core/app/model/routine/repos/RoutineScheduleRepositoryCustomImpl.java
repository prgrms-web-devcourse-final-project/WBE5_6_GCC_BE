package com.honlife.core.app.model.routine.repos;

import com.honlife.core.app.model.category.domain.QCategory;
import com.honlife.core.app.model.dashboard.dto.CategoryCountDTO;
import com.honlife.core.app.model.dashboard.dto.DayRoutineCountDTO;
import com.honlife.core.app.model.dashboard.dto.RoutineTotalCountDTO;
import com.honlife.core.app.model.routine.domain.QRoutine;
import com.honlife.core.app.model.routine.domain.QRoutineSchedule;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.honlife.core.app.model.member.domain.QMember;

@Repository
@RequiredArgsConstructor
public class RoutineScheduleRepositoryCustomImpl implements RoutineScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QRoutineSchedule qRoutineSchedule =QRoutineSchedule.routineSchedule;
    private final QRoutine qRoutine =QRoutine.routine;
    private final QCategory category=QCategory.category;
    private final QMember qMember = QMember.member;

    public Long getCountOfNotCompletedMemberSchedule(LocalDate date, String userEmail) {
        return queryFactory
            .select(qRoutineSchedule.count())
            .from(qRoutineSchedule)
            .leftJoin(qRoutineSchedule.routine, qRoutine)
            .leftJoin(qRoutine.member, qMember)
            .where(
                qRoutineSchedule.scheduledDate.eq(date),
                qMember.email.eq(userEmail),
                qRoutineSchedule.isDone.isFalse()
            )
            .fetchOne();
    }
    @Override
    public RoutineTotalCountDTO countRoutineScheduleByMemberAndDateBetweenAndIsDone(String userEmail, LocalDate startDate, LocalDate endDate) {
        return queryFactory
            .select(Projections.constructor(RoutineTotalCountDTO.class,
                    qRoutineSchedule.count(), // 총 루틴 수
                    JPAExpressions // 완료한 루틴 수
                        .select(qRoutineSchedule.count())
                        .from(qRoutineSchedule)
                        .where((qRoutineSchedule.scheduledDate.goe(startDate))
                            .and(qRoutineSchedule.scheduledDate.lt(endDate))
                            .and(qRoutineSchedule.isDone)
                            .and(qRoutineSchedule.routine.member.email.eq(userEmail))
                )))
            .from(qRoutineSchedule)
            .where((qRoutine.member.email.eq(userEmail))
                .and(qRoutineSchedule.scheduledDate.goe(startDate))
                .and(qRoutineSchedule.scheduledDate.lt(endDate))
            )
            .fetchOne();
    }

    @Override
    public List<DayRoutineCountDTO> countRoutineSchedulesGroupByDateBetween(String userEmail, LocalDate startDate, LocalDate endDate) {
        QRoutineSchedule outerRoutineSchedule = new QRoutineSchedule("outerRoutineSchedule");

         return queryFactory
            .select(Projections.constructor(DayRoutineCountDTO.class,
                outerRoutineSchedule.scheduledDate, // 해당 날짜
                outerRoutineSchedule.count(), // 해당 날짜의 총 루틴 수
                JPAExpressions.select(qRoutineSchedule.count()) // 해당 날짜의 완료한 루틴 수
                    .from(qRoutineSchedule)
                    .where((qRoutineSchedule.scheduledDate.eq(outerRoutineSchedule.scheduledDate))
                        .and(qRoutineSchedule.routine.member.email.eq(userEmail)
                        .and(qRoutineSchedule.isDone))
                    )
                )
            )
            .from(outerRoutineSchedule)
            .where((outerRoutineSchedule.routine.member.email.eq(userEmail))
                .and(outerRoutineSchedule.scheduledDate.goe(startDate))
                .and(outerRoutineSchedule.scheduledDate.lt(endDate))
            )
             .groupBy(outerRoutineSchedule.scheduledDate)
             .fetch();
    }

    @Override
    public List<CategoryCountDTO> countRoutineSchedulesGroupByCategory(String userEmail, LocalDate startDate, LocalDate endDate, Boolean isDone) {
        QCategory parent= new QCategory("parent");

        return queryFactory
            .select(Projections.constructor(CategoryCountDTO.class,
                parent.name, // 부모 카테고리의 이름
                category.name, // 루틴에 저장된 카테고리의 이름
                qRoutineSchedule.count() // 카테고리 별 루틴 수
            ))
            .from(qRoutineSchedule)
            .leftJoin(qRoutineSchedule.routine.category, category)
            .leftJoin(category.parent, parent)
            .where(qRoutine.member.email.eq(userEmail)
                .and(qRoutineSchedule.scheduledDate.goe(startDate))
                .and(qRoutineSchedule.scheduledDate.lt(endDate))
                .and(isDone != null ? qRoutineSchedule.isDone.eq(isDone) : null))
            .groupBy(category, parent)
            .orderBy(qRoutineSchedule.count().desc())
            .fetch();
    }

    @Override
    public List<RoutineSchedule> findAllByDateBetween(String userEmail, LocalDate startDate, LocalDate endDate) {
        return queryFactory.select(qRoutineSchedule)
            .from(qRoutineSchedule)
            .leftJoin(qRoutineSchedule.routine, qRoutine).fetchJoin()
            .leftJoin(qRoutine.category, category).fetchJoin()
            .where((qRoutine.member.email.eq(userEmail))
                .and(qRoutineSchedule.scheduledDate.goe(startDate))
                .and(qRoutineSchedule.scheduledDate.lt(endDate))
            ).fetch();
    }
}