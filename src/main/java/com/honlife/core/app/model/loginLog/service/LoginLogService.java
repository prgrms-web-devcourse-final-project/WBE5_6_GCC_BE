package com.honlife.core.app.model.loginLog.service;

import com.honlife.core.app.model.member.service.MemberService;
import java.time.LocalDateTime;
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
import com.honlife.core.infra.util.NotFoundException;


@Service
@RequiredArgsConstructor
public class LoginLogService {

    private final LoginLogRepository loginLogRepository;
    private final MemberRepository memberRepository;

    public List<LoginLogDTO> findAll() {
        final List<LoginLog> loginLogs = loginLogRepository.findAll(Sort.by("id"));
        return loginLogs.stream()
            .map(loginLog -> mapToDTO(loginLog, new LoginLogDTO()))
            .toList();
    }

    public LoginLogDTO get(final Long id) {
        return loginLogRepository.findById(id)
            .map(loginLog -> mapToDTO(loginLog, new LoginLogDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public Long create(final LoginLogDTO loginLogDTO) {
        final LoginLog loginLog = new LoginLog();
        mapToEntity(loginLogDTO, loginLog);
        return loginLogRepository.save(loginLog).getId();
    }

    public void update(final Long id, final LoginLogDTO loginLogDTO) {
        final LoginLog loginLog = loginLogRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(loginLogDTO, loginLog);
        loginLogRepository.save(loginLog);
    }

    public void delete(final Long id) {
        loginLogRepository.deleteById(id);
    }

    private LoginLogDTO mapToDTO(final LoginLog loginLog, final LoginLogDTO loginLogDTO) {
        loginLogDTO.setId(loginLog.getId());
        loginLogDTO.setTime(loginLog.getTime());
        loginLogDTO.setMember(loginLog.getMember() == null ? null : loginLog.getMember().getId());
        return loginLogDTO;
    }

    private LoginLog mapToEntity(final LoginLogDTO loginLogDTO, final LoginLog loginLog) {
        loginLog.setTime(loginLogDTO.getTime());
        final Member member = loginLogDTO.getMember() == null ? null : memberRepository.findById(loginLogDTO.getMember())
            .orElseThrow(() -> new NotFoundException("member not found"));
        loginLog.setMember(member);
        return loginLog;
    }

    /**
     * Save new login log when user login
     * @param email userEmail
     */
    @Async
    public void newLog(String email) {
        Member member = memberRepository.findByEmailIgnoreCase(email);
        LoginLog loginLog = LoginLog.builder()
            .member(member)
            .build();
        loginLogRepository.save(loginLog);
    }


}
