package com.honlife.core.app.model.withdraw.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.withdraw.domain.WithdrawReason;


public interface WithdrawReasonRepository extends JpaRepository<WithdrawReason, Long> {
}
