package spring.grepp.honlife.app.model.loginLog.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.app.model.loginLog.domain.LoginLog;
import spring.grepp.honlife.app.model.loginLog.dto.LoginLogDTO;
import spring.grepp.honlife.app.model.loginLog.repos.LoginLogRepository;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.member.repos.MemberRepository;
import spring.grepp.honlife.infra.util.NotFoundException;


@Service
public class LoginLogService {

    private final LoginLogRepository loginLogRepository;
    private final MemberRepository memberRepository;

    public LoginLogService(final LoginLogRepository loginLogRepository,
        final MemberRepository memberRepository) {
        this.loginLogRepository = loginLogRepository;
        this.memberRepository = memberRepository;
    }

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

}
