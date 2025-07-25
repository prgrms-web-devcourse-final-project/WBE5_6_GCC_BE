package com.honlife.core.app.model.withdraw.repos;

import com.honlife.core.app.model.withdraw.code.WithdrawType;
import com.honlife.core.app.model.withdraw.domain.QWithdrawReason;
import com.honlife.core.app.model.withdraw.domain.WithdrawReason;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WithdrawReasonRepositoryCustomImpl implements WithdrawReasonRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QWithdrawReason withdrawReason=QWithdrawReason.withdrawReason;

    @Override
    public Page<WithdrawReason> findPagedByDate(Pageable pageable, LocalDateTime startDate,
        LocalDateTime endDate) {

        BooleanBuilder builder = new BooleanBuilder();

        if (startDate != null) {
            builder.and(withdrawReason.createdAt.goe(startDate));
        }
        if (endDate != null) {
            builder.and(withdrawReason.createdAt.loe(endDate));
        }

        List<WithdrawReason> withdrawReasons = queryFactory
            .select(withdrawReason)
            .from(withdrawReason)
            .where(builder
                .and(withdrawReason.type.eq(WithdrawType.ETC)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(withdrawReason.count())
            .from(withdrawReason)
            .where(builder);

        return PageableExecutionUtils.getPage(withdrawReasons, pageable, countQuery::fetchOne);

    }
}
