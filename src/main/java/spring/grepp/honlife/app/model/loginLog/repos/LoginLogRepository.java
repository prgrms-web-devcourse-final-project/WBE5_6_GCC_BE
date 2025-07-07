package spring.grepp.honlife.app.model.loginLog.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.loginLog.domain.LoginLog;
import spring.grepp.honlife.app.model.member.domain.Member;


public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

    LoginLog findFirstByMember(Member member);

}
