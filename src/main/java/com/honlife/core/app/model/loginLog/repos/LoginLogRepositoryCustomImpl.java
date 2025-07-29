package com.honlife.core.app.model.loginLog.repos;

import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.honlife.core.app.model.loginLog.domain.QLoginLog.loginLog;

@Repository
@RequiredArgsConstructor
public class LoginLogRepositoryCustomImpl implements LoginLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // === 일간 통계 ===
    @Override
    public List<Object[]> findVisitorsByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DateTemplate<LocalDate> dateFunc = Expressions.dateTemplate(LocalDate.class,
            "DATE({0})", loginLog.time);

        return queryFactory.select(
                dateFunc,
                loginLog.member.countDistinct()
            )
            .from(loginLog)
            .where(loginLog.time.between(startDateTime, endDateTime))
            .groupBy(dateFunc)
            .orderBy(dateFunc.asc())
            .fetch()
            .stream()
            .map(tuple -> {
                java.sql.Date sqlDate = tuple.get(0, java.sql.Date.class);
                LocalDate localDate = sqlDate != null ? sqlDate.toLocalDate() : null;
                Long count = tuple.get(1, Long.class);
                return new Object[]{localDate, count};
            })
            .toList();
    }

    // === 주간 통계 ===
    @Override
    public List<Object[]> findVisitorsByWeek(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DateTemplate<LocalDate> weekStartFunc = Expressions.dateTemplate(LocalDate.class,
            "CAST(DATE_TRUNC('week', {0}) AS date)", loginLog.time);

        return queryFactory.select(
                weekStartFunc,
                loginLog.member.countDistinct()
            )
            .from(loginLog)
            .where(loginLog.time.between(startDateTime, endDateTime))
            .groupBy(weekStartFunc)
            .orderBy(weekStartFunc.asc())
            .fetch()
            .stream()
            .map(tuple -> {
                java.sql.Date sqlDate = tuple.get(0, java.sql.Date.class);
                LocalDate localDate = sqlDate != null ? sqlDate.toLocalDate() : null;
                Long count = tuple.get(1, Long.class);
                return new Object[]{localDate, count};
            })
            .toList();
    }

    // === 월간 통계 ===
    @Override
    public List<Object[]> findVisitorsByMonth(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        DateTemplate<LocalDate> monthStartFunc = Expressions.dateTemplate(LocalDate.class,
            "CAST(DATE_TRUNC('month', {0}) AS date)", loginLog.time);

        return queryFactory.select(
                monthStartFunc,
                loginLog.member.countDistinct()
            )
            .from(loginLog)
            .where(loginLog.time.between(startDateTime, endDateTime))
            .groupBy(monthStartFunc)
            .orderBy(monthStartFunc.asc())
            .fetch()
            .stream()
            .map(tuple -> {
                java.sql.Date sqlDate = tuple.get(0, java.sql.Date.class);
                LocalDate localDate = sqlDate != null ? sqlDate.toLocalDate() : null;
                Long count = tuple.get(1, Long.class);
                return new Object[]{localDate, count};
            })
            .toList();
    }
}