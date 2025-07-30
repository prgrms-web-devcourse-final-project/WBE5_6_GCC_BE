package com.honlife.core.app.model.member.service;

import com.honlife.core.app.model.point.code.PointLogType;
import com.honlife.core.app.model.point.code.PointSourceType;
import com.honlife.core.app.model.point.service.PointLogService;
import com.honlife.core.app.model.point.service.PointPolicyService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberPoint;
import com.honlife.core.app.model.member.model.MemberPointDTO;
import com.honlife.core.app.model.member.repos.MemberPointRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;


@Service
@RequiredArgsConstructor
public class MemberPointService {

    private final MemberPointRepository memberPointRepository;
    private final MemberRepository memberRepository;
    private final PointPolicyService pointPolicyService;
    private final PointLogService pointLogService;

    public List<MemberPointDTO> findAll() {
        final List<MemberPoint> memberPoints = memberPointRepository.findAll(Sort.by("id"));
        return memberPoints.stream()
            .map(memberPoint -> mapToDTO(memberPoint, new MemberPointDTO()))
            .toList();
    }

    public MemberPointDTO get(final Long id) {
        return memberPointRepository.findById(id)
            .map(memberPoint -> mapToDTO(memberPoint, new MemberPointDTO()))
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_POINT));
    }

    public Long create(final MemberPointDTO memberPointDTO) {
        final MemberPoint memberPoint = new MemberPoint();
        mapToEntity(memberPointDTO, memberPoint);
        return memberPointRepository.save(memberPoint).getId();
    }

    public void update(final Long id, final MemberPointDTO memberPointDTO) {
        final MemberPoint memberPoint = memberPointRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_POINT));
        mapToEntity(memberPointDTO, memberPoint);
        memberPointRepository.save(memberPoint);
    }

    public void delete(final Long id) {
        memberPointRepository.deleteById(id);
    }

    private MemberPointDTO mapToDTO(final MemberPoint memberPoint,
        final MemberPointDTO memberPointDTO) {
        memberPointDTO.setCreatedAt(memberPoint.getCreatedAt());
        memberPointDTO.setUpdatedAt(memberPoint.getUpdatedAt());
        memberPointDTO.setIsActive(memberPoint.getIsActive());
        memberPointDTO.setId(memberPoint.getId());
        memberPointDTO.setPoint(memberPoint.getPoint());
        memberPointDTO.setMember(memberPoint.getMember() == null ? null : memberPoint.getMember().getId());
        return memberPointDTO;
    }

    private MemberPoint mapToEntity(final MemberPointDTO memberPointDTO,
        final MemberPoint memberPoint) {
        memberPoint.setCreatedAt(memberPointDTO.getCreatedAt());
        memberPoint.setUpdatedAt(memberPointDTO.getUpdatedAt());
        memberPoint.setIsActive(memberPointDTO.getIsActive());
        memberPoint.setPoint(memberPointDTO.getPoint());
        final Member member = memberPointDTO.getMember() == null ? null : memberRepository.findById(memberPointDTO.getMember())
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));
        memberPoint.setMember(member);
        return memberPoint;
    }

    public boolean memberExists(final Long id) {
        return memberPointRepository.existsByMemberId(id);
    }

    /**
     * 멤버 아이디를 통해 조회하여 연관된 모든 멤버 포인트를 삭제합니다.
     * @param memberId 멤버 식별아이디
     */
    @Transactional
    public void softDropMemberPointByMemberId(Long memberId) {
        memberPointRepository.softDropByMemberId(memberId);
    }

    /**
     * memberId를 통해 MemberPoint 정보를 가져옵니다.
     *
     * @param memberId 사용자 ID
     * @return Optional<MemberPoint>
     */
    public Optional<MemberPoint> getPointByMemberId(Long memberId) {
        return memberPointRepository.findByMemberId(memberId);
    }

    /**
     * find MemberPoint via member's email and map to MemberPointDTO
     * @param email member's email
     * @return {@link MemberPointDTO}
     */
    public MemberPointDTO getMemberPoint(String email) {
        MemberPoint memberPoint = memberPointRepository.findByMember_Email(email)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_POINT));
        return MemberPointDTO.builder()
            .id(memberPoint.getId())
            .point(memberPoint.getPoint())
            .build();
    }

    /**
     * Search for the number of points to be granted from PointPolicy DB<br>
     * and add to member's current point.<br>
     * Then, save a log
     * @param userEmail user's email
     * @param key reference_key
     * @param type type of point source
     */
    @Transactional
    public void addPoint(String userEmail, String key, PointSourceType type) {
        Integer points = pointPolicyService.getPoint(key, type);

        MemberPoint memberPoint = memberPointRepository.findByMember_EmailAndIsActive(userEmail, true)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_POINT));

        Integer CurrentPoints = memberPoint.getPoint();
        memberPoint.setPoint(CurrentPoints + points);
        memberPointRepository.save(memberPoint);

        // Save Log
        pointLogService.saveLog(userEmail, PointLogType.GET, key);
    }

    @Transactional
    public void subtractPoint(String userEmail, String key, PointSourceType type) {

        Integer points = pointPolicyService.getPoint(key, type);

        MemberPoint memberPoint = memberPointRepository.findByMember_EmailAndIsActive(userEmail, true)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_POINT));

        int newPoints = memberPoint.getPoint() - points;

        memberPoint.setPoint(Math.max(newPoints, 0));
        memberPointRepository.save(memberPoint);

        // Save Log
        pointLogService.saveLog(userEmail, PointLogType.GET, key);
    }

    /**
     * 회원에게 포인트 추가 지급
     * @param memberId 회원 ID
     * @param pointToAdd 추가할 포인트
     * @return 업데이트 후 총 포인트
     */
    @Transactional
    public int addPointToMember(Long memberId, int pointToAdd) {
        Optional<MemberPoint> memberPoint = getPointByMemberId(memberId)
            .filter(mp -> Boolean.TRUE.equals(mp.getIsActive()));

        if (memberPoint.isEmpty()) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_POINT);
        }

        MemberPoint point = memberPoint.get();
        int currentPoint = point.getPoint();
        int newTotalPoint = currentPoint + pointToAdd;

        // 포인트 업데이트
        MemberPointDTO pointDTO = get(point.getId());
        pointDTO.setPoint(newTotalPoint);
        update(point.getId(), pointDTO);

        return newTotalPoint;
    }
}
