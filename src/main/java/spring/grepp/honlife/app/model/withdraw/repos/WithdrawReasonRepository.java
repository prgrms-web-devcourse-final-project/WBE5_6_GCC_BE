package spring.grepp.honlife.app.model.withdraw.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.withdraw.domain.WithdrawReason;


public interface WithdrawReasonRepository extends JpaRepository<WithdrawReason, Long> {
}
