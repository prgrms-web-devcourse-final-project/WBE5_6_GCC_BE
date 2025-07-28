package com.honlife.core.app.model.loginLog.repos;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.loginLog.domain.LoginLog;
import com.honlife.core.app.model.member.domain.Member;


public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

    LoginLog findFirstByMember(Member member);

    Optional<LoginLog> findByMember_EmailAndTimeBetween(String memberEmail, LocalDateTime timeAfter, LocalDateTime timeBefore);
}
