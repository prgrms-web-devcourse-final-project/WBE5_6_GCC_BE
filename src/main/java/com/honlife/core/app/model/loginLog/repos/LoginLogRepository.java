package com.honlife.core.app.model.loginLog.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.loginLog.domain.LoginLog;
import com.honlife.core.app.model.member.domain.Member;


public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

    LoginLog findFirstByMember(Member member);

}
