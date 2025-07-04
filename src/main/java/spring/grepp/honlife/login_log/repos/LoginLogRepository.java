package spring.grepp.honlife.login_log.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.login_log.domain.LoginLog;
import spring.grepp.honlife.member.domain.Member;


public interface LoginLogRepository extends JpaRepository<LoginLog, Integer> {

    LoginLog findFirstByMember(Member member);

}
