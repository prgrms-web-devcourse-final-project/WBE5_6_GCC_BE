package com.honlife.core.app.model.point.service;

import com.honlife.core.app.model.point.code.PointLogType;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.app.model.point.code.PointLogType;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.point.domain.PointLog;
import com.honlife.core.app.model.point.dto.PointLogDTO;
import com.honlife.core.app.model.point.repos.PointLogRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PointLogService {

    private final MemberService memberService;
    private final PointLogRepository pointLogRepository;
    private final MemberRepository memberRepository;

    public List<PointLogDTO> findAll() {
        final List<PointLog> pointLogs = pointLogRepository.findAll(Sort.by("id"));
        return pointLogs.stream()
                .map(pointLog -> mapToDTO(pointLog, new PointLogDTO()))
                .toList();
    }

    public PointLogDTO get(final Long id) {
        return pointLogRepository.findById(id)
                .map(pointLog -> mapToDTO(pointLog, new PointLogDTO()))
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND));
    }

    public Long create(final PointLogDTO pointLogDTO) {
        final PointLog pointLog = new PointLog();
        mapToEntity(pointLogDTO, pointLog);
        return pointLogRepository.save(pointLog).getId();
    }

    public void update(final Long id, final PointLogDTO pointLogDTO) {
        final PointLog pointLog = pointLogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND));
        mapToEntity(pointLogDTO, pointLog);
        pointLogRepository.save(pointLog);
    }

    public void delete(final Long id) {
        pointLogRepository.deleteById(id);
    }

    /**
     * 포인트 로그 기록
     * @param email 회원 이메일
     * @param point 포인트 양
     * @param reason 지급/사용 사유
     * @param type 포인트 타입 (GET/USE)
     */
    @Transactional
    public void recordPointLog(String email, int point, String reason, PointLogType type) {
        Member member = memberService.getMemberByEmail(email);

        PointLog pointLog = PointLog.builder()
            .member(member)
            .type(type)
            .point(point)
            .reason(reason)
            .time(LocalDateTime.now())
            .build();

        pointLogRepository.save(pointLog);
    }

    private PointLogDTO mapToDTO(final PointLog pointLog, final PointLogDTO pointLogDTO) {
        pointLogDTO.setId(pointLog.getId());
        pointLogDTO.setType(pointLog.getType());
        pointLogDTO.setPoint(pointLog.getPoint());
        pointLogDTO.setReason(pointLog.getReason());
        pointLogDTO.setTime(pointLog.getTime());
        pointLogDTO.setMember(pointLog.getMember() == null ? null : pointLog.getMember().getId());
        return pointLogDTO;
    }

    private PointLog mapToEntity(final PointLogDTO pointLogDTO, final PointLog pointLog) {
        pointLog.setType(pointLogDTO.getType());
        pointLog.setPoint(pointLogDTO.getPoint());
        pointLog.setReason(pointLogDTO.getReason());
        pointLog.setTime(pointLogDTO.getTime());
        final Member member = pointLogDTO.getMember() == null ? null : memberRepository.findById(pointLogDTO.getMember())
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));
        pointLog.setMember(member);
        return pointLog;
    }

    /**
     * Save point get or consume log
     * @param userEmail
     * @param pointLogType
     * @param key {@code Quest} or {@code Item} or {@code Badge} key value
     */
    @Async
    @Transactional
    public void saveLog(String userEmail, PointLogType pointLogType, String key) {
        Member member = memberRepository.findByEmailIgnoreCase(userEmail);
        pointLogRepository.save(
            PointLog.builder()
                .member(member)
                .type(pointLogType)
                .reason(key)
                .time(LocalDateTime.now())
                .build()
        );
    }
}
