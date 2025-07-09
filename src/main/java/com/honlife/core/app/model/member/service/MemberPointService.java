package com.honlife.core.app.model.member.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberPoint;
import com.honlife.core.app.model.member.model.MemberPointDTO;
import com.honlife.core.app.model.member.repos.MemberPointRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.util.NotFoundException;
import com.honlife.core.infra.util.ReferencedWarning;


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
            .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberPointDTO memberPointDTO) {
        final MemberPoint memberPoint = new MemberPoint();
        mapToEntity(memberPointDTO, memberPoint);
        return memberPointRepository.save(memberPoint).getId();
    }

    public void update(final Long id, final MemberPointDTO memberPointDTO) {
        final MemberPoint memberPoint = memberPointRepository.findById(id)
            .orElseThrow(NotFoundException::new);
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
            .orElseThrow(() -> new NotFoundException("member not found"));
        memberPoint.setMember(member);
        return memberPoint;
    }

    public boolean memberExists(final Long id) {
        return memberPointRepository.existsByMemberId(id);
    }

}
