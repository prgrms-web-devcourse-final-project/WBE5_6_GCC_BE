package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.member.domain.QMember;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QMember member = QMember.member;

    @Override
    public boolean isEmailVerified(String email) {
        return Boolean.TRUE.equals(queryFactory
            .select(member.isVerified)
            .from(member)
            .where(member.email.equalsIgnoreCase(email))
            .fetchOne());
    }

    @Override
    public void softDropMember(String userEmail) {
        queryFactory
            .update(member)
            .set(member.isActive, false)
            .where(member.email.eq(userEmail))
            .execute();
    }

    // === 일간 통계 ===
    @Override
    public List<Object[]> findTotalMembersByDateRange(LocalDate startDate, LocalDate endDate) {
        DateTemplate<LocalDate> dateFunc = Expressions.dateTemplate(LocalDate.class,
            "DATE({0})", member.createdAt);

        return queryFactory.select(
                dateFunc,
                member.count()
            )
            .from(member)
            .where(
                member.isActive.eq(true)
                    .and(member.createdAt.loe(endDate.atTime(23, 59, 59)))
            )
            .groupBy(dateFunc)
            .having(dateFunc.between(startDate, endDate))
            .orderBy(dateFunc.asc())
            .fetch()
            .stream()
            .map(tuple -> new Object[]{tuple.get(0, LocalDate.class), tuple.get(1, Long.class)})
            .toList();
    }

    @Override
    public List<Object[]> findNewMembersByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DateTemplate<LocalDate> dateFunc = Expressions.dateTemplate(LocalDate.class,
            "DATE({0})", member.createdAt);

        return queryFactory.select(
                dateFunc,
                member.count()
            )
            .from(member)
            .where(
                member.isActive.eq(true)
                    .and(member.createdAt.between(startDateTime, endDateTime))
            )
            .groupBy(dateFunc)
            .orderBy(dateFunc.asc())
            .fetch()
            .stream()
            .map(tuple -> new Object[]{tuple.get(0, LocalDate.class), tuple.get(1, Long.class)})
            .toList();
    }

    // === 주간 통계 ===
    @Override
    public List<Object[]> findTotalMembersByWeek(LocalDate startDate, LocalDate endDate) {
        DateTemplate<LocalDate> weekStartFunc = Expressions.dateTemplate(LocalDate.class,
            "DATE_TRUNC('week', {0})::date", member.createdAt);

        return queryFactory.select(
                weekStartFunc,
                member.count()
            )
            .from(member)
            .where(
                member.isActive.eq(true)
                    .and(member.createdAt.loe(endDate.atTime(23, 59, 59)))
            )
            .groupBy(weekStartFunc)
            .having(weekStartFunc.between(startDate, endDate))
            .orderBy(weekStartFunc.asc())
            .fetch()
            .stream()
            .map(tuple -> new Object[]{tuple.get(0, LocalDate.class), tuple.get(1, Long.class)})
            .toList();
    }

    @Override
    public List<Object[]> findNewMembersByWeek(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DateTemplate<LocalDate> weekStartFunc = Expressions.dateTemplate(LocalDate.class,
            "DATE_TRUNC('week', {0})::date", member.createdAt);

        return queryFactory.select(
                weekStartFunc,
                member.count()
            )
            .from(member)
            .where(
                member.isActive.eq(true)
                    .and(member.createdAt.between(startDateTime, endDateTime))
            )
            .groupBy(weekStartFunc)
            .orderBy(weekStartFunc.asc())
            .fetch()
            .stream()
            .map(tuple -> new Object[]{tuple.get(0, LocalDate.class), tuple.get(1, Long.class)})
            .toList();
    }

    // === 월간 통계 ===
    @Override
    public List<Object[]> findTotalMembersByMonth(LocalDate startDate, LocalDate endDate) {
        DateTemplate<LocalDate> monthStartFunc = Expressions.dateTemplate(LocalDate.class,
            "DATE_TRUNC('month', {0})::date", member.createdAt);

        return queryFactory.select(
                monthStartFunc,
                member.count()
            )
            .from(member)
            .where(
                member.isActive.eq(true)
                    .and(member.createdAt.loe(endDate.atTime(23, 59, 59)))
            )
            .groupBy(monthStartFunc)
            .having(monthStartFunc.between(startDate, endDate))
            .orderBy(monthStartFunc.asc())
            .fetch()
            .stream()
            .map(tuple -> new Object[]{tuple.get(0, LocalDate.class), tuple.get(1, Long.class)})
            .toList();
    }

    @Override
    public List<Object[]> findNewMembersByMonth(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DateTemplate<LocalDate> monthStartFunc = Expressions.dateTemplate(LocalDate.class,
            "DATE_TRUNC('month', {0})::date", member.createdAt);

        return queryFactory.select(
                monthStartFunc,
                member.count()
            )
            .from(member)
            .where(
                member.isActive.eq(true)
                    .and(member.createdAt.between(startDateTime, endDateTime))
            )
            .groupBy(monthStartFunc)
            .orderBy(monthStartFunc.asc())
            .fetch()
            .stream()
            .map(tuple -> new Object[]{tuple.get(0, LocalDate.class), tuple.get(1, Long.class)})
            .toList();
    }
}
