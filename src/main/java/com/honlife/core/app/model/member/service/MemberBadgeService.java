package com.honlife.core.app.model.member.service;

import com.honlife.core.app.model.badge.domain.Badge;
import com.honlife.core.app.model.badge.repos.BadgeRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberBadge;
import com.honlife.core.app.model.member.model.MemberBadgeDTO;
import com.honlife.core.app.model.member.model.MemberBadgeDetailDTO;
import com.honlife.core.app.model.member.model.MemberDTO;
import com.honlife.core.app.model.member.repos.MemberBadgeRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.util.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberBadgeService {

    private final MemberBadgeRepository memberBadgeRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final BadgeRepository badgeRepository;

    public List<MemberBadgeDTO> findAll() {
        final List<MemberBadge> memberBadges = memberBadgeRepository.findAll(Sort.by("id"));
        return memberBadges.stream()
                .map(memberBadge -> mapToDTO(memberBadge, new MemberBadgeDTO()))
                .toList();
    }

    public MemberBadgeDTO get(final Long id) {
        return memberBadgeRepository.findById(id)
                .map(memberBadge -> mapToDTO(memberBadge, new MemberBadgeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberBadgeDTO memberBadgeDTO) {
        final MemberBadge memberBadge = new MemberBadge();
        mapToEntity(memberBadgeDTO, memberBadge);
        return memberBadgeRepository.save(memberBadge).getId();
    }

    public void update(final Long id, final MemberBadgeDTO memberBadgeDTO) {
        final MemberBadge memberBadge = memberBadgeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(memberBadgeDTO, memberBadge);
        memberBadgeRepository.save(memberBadge);
    }

    /**
     * 특정 사용자가 보유한 모든 배지의 상세 정보 조회 (이메일로)
     * @param email 사용자 이메일
     * @return 사용자가 보유한 배지 상세 정보 리스트
     */
    @Transactional(readOnly = true)
    public List<MemberBadgeDetailDTO> getMemberBadgeDetails(String email) {
        // 1. MemberService를 통해 사용자 조회
        MemberDTO memberDTO = memberService.findMemberByEmail(email);

        // 2. 사용자가 보유한 배지들 조회
        List<MemberBadge> memberBadges = memberBadgeRepository.findByMemberId(memberDTO.getId());

        // 3. 활성화된 배지만 필터링하고 상세 정보 DTO로 변환
        return memberBadges.stream()
            .filter(mb -> mb.getIsActive())
            .map(memberBadge -> {
                Badge badge = memberBadge.getBadge();

                return MemberBadgeDetailDTO.builder()
                    // MemberBadge 정보
                    .memberBadgeId(memberBadge.getId())
                    .receivedDate(memberBadge.getCreatedAt())

                    // Badge 정보
                    .badgeId(badge.getId())
                    .badgeKey(badge.getKey())
                    .badgeName(badge.getName())
                    .badgeTier(badge.getTier())
                    .badgeInfo(badge.getInfo())
                    .build();
            })
            .toList();
    }

    public void delete(final Long id) {
        memberBadgeRepository.deleteById(id);
    }

    private MemberBadgeDTO mapToDTO(final MemberBadge memberBadge,
            final MemberBadgeDTO memberBadgeDTO) {
        memberBadgeDTO.setCreatedAt(memberBadge.getCreatedAt());
        memberBadgeDTO.setUpdatedAt(memberBadge.getUpdatedAt());
        memberBadgeDTO.setIsActive(memberBadge.getIsActive());
        memberBadgeDTO.setId(memberBadge.getId());
        memberBadgeDTO.setMember(memberBadge.getMember() == null ? null : memberBadge.getMember().getId());
        memberBadgeDTO.setBadge(memberBadge.getBadge() == null ? null : memberBadge.getBadge().getId());
        return memberBadgeDTO;
    }

    private MemberBadge mapToEntity(final MemberBadgeDTO memberBadgeDTO,
            final MemberBadge memberBadge) {
        memberBadge.setCreatedAt(memberBadgeDTO.getCreatedAt());
        memberBadge.setUpdatedAt(memberBadgeDTO.getUpdatedAt());
        memberBadge.setIsActive(memberBadgeDTO.getIsActive());
        final Member member = memberBadgeDTO.getMember() == null ? null : memberRepository.findById(memberBadgeDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        memberBadge.setMember(member);
        final Badge badge = memberBadgeDTO.getBadge() == null ? null : badgeRepository.findById(memberBadgeDTO.getBadge())
                .orElseThrow(() -> new NotFoundException("badge not found"));
        memberBadge.setBadge(badge);
        return memberBadge;
    }

}
