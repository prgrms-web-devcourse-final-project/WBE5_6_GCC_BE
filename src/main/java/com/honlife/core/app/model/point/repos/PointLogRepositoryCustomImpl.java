package com.honlife.core.app.model.point.repos;

import com.honlife.core.app.model.point.code.PointLogType;
import com.honlife.core.app.model.point.domain.QPointLog;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointLogRepositoryCustomImpl implements PointLogRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QPointLog pointLog = QPointLog.pointLog;


    @Override
    public Integer getTotalPointByDate(LocalDateTime startDate, LocalDateTime endDate,
        PointLogType pointLogType, String userEmail) {
            return queryFactory
                .select(pointLog.point.sum())
                .from(pointLog)
                .where(
                    (pointLog.member.email.eq(userEmail))
                        .and(pointLog.time.goe(startDate))
                        .and(pointLog.time.lt(endDate))
                        .and(pointLog.type.eq(pointLogType))
                ).fetchOne();
    }
}
