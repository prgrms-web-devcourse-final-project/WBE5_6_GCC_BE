package com.honlife.core.app.model.routine.repos;

import com.honlife.core.app.model.member.domain.QMember;
import com.honlife.core.app.model.routine.domain.QRoutine;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoutineRepositoryCustomImpl implements RoutineRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QRoutine routine = QRoutine.routine;

    public void softDropByMemberId(Long memberId){
        queryFactory
            .update(routine)
            .set(routine.isActive, false)
            .where(routine.member.id.eq(memberId))
            .execute();
    };
}
