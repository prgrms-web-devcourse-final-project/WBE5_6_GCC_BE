package com.honlife.core.app.model.loginLog.service;

import com.honlife.core.infra.response.ResponseCode;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.loginLog.domain.LoginLog;
import com.honlife.core.app.model.loginLog.dto.LoginLogDTO;
import com.honlife.core.app.model.loginLog.repos.LoginLogRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;


@Service
@RequiredArgsConstructor
public class LoginLogService {

    private final LoginLogRepository loginLogRepository;
    private final MemberRepository memberRepository;

    /**
     * Save new login log when user login
     * @param email userEmail
     */
    @Async
    @Transactional
    public void newLog(String email) {
        Member member = memberRepository.findByEmailIgnoreCase(email);
        LoginLog loginLog = LoginLog.builder()
            .member(member)
            .build();
        loginLogRepository.save(loginLog);
    }


}
