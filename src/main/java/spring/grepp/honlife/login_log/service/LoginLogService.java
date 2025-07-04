package spring.grepp.honlife.login_log.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.login_log.domain.LoginLog;
import spring.grepp.honlife.login_log.model.LoginLogDTO;
import spring.grepp.honlife.login_log.repos.LoginLogRepository;
import spring.grepp.honlife.member.domain.Member;
import spring.grepp.honlife.member.repos.MemberRepository;
import spring.grepp.honlife.util.NotFoundException;


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

    public LoginLogDTO get(final Integer id) {
        return loginLogRepository.findById(id)
                .map(loginLog -> mapToDTO(loginLog, new LoginLogDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final LoginLogDTO loginLogDTO) {
        final LoginLog loginLog = new LoginLog();
        mapToEntity(loginLogDTO, loginLog);
        return loginLogRepository.save(loginLog).getId();
    }

    public void update(final Integer id, final LoginLogDTO loginLogDTO) {
        final LoginLog loginLog = loginLogRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(loginLogDTO, loginLog);
        loginLogRepository.save(loginLog);
    }

    public void delete(final Integer id) {
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
