package com.honlife.core.app.model.loginLog.repos;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.loginLog.domain.LoginLog;
import com.honlife.core.app.model.member.domain.Member;


public interface LoginLogRepository extends JpaRepository<LoginLog, Long>, LoginLogRepositoryCustom {

    LoginLog findFirstByMember(Member member);

    List<LoginLog> findByMember_EmailAndTimeBetween(String memberEmail, LocalDateTime timeAfter, LocalDateTime timeBefore);

    Boolean existsByMember_EmailAndTimeBetween(String memberEmail, LocalDateTime timeAfter, LocalDateTime timeBefore);
}
