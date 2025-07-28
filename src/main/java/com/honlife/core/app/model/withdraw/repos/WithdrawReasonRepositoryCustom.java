package com.honlife.core.app.model.withdraw.repos;

import com.honlife.core.app.model.withdraw.code.WithdrawType;
import com.honlife.core.app.model.withdraw.domain.WithdrawReason;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WithdrawReasonRepositoryCustom {

    Page<WithdrawReason> findPagedByDateBetween(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);

    Map<WithdrawType, Long> countByType(LocalDateTime startDate, LocalDateTime endDate);
}
