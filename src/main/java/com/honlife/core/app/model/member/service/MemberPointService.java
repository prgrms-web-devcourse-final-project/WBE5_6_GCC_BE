package com.honlife.core.app.model.member.service;

import com.honlife.core.infra.response.ResponseCode;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberPoint;
import com.honlife.core.app.model.member.model.MemberPointDTO;
import com.honlife.core.app.model.member.repos.MemberPointRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;


@Service
public class MemberPointService {

    private final MemberPointRepository memberPointRepository;
    private final MemberRepository memberRepository;

    public MemberPointService(final MemberPointRepository memberPointRepository,
        final MemberRepository memberRepository) {
        this.memberPointRepository = memberPointRepository;
        this.memberRepository = memberRepository;
    }

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
}
