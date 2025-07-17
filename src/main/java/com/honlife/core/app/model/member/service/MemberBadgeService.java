package com.honlife.core.app.model.member.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.badge.domain.Badge;
import com.honlife.core.app.model.badge.repos.BadgeRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberBadge;
import com.honlife.core.app.model.member.model.MemberBadgeDTO;
import com.honlife.core.app.model.member.repos.MemberBadgeRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.util.NotFoundException;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MemberBadgeService {

    private final MemberBadgeRepository memberBadgeRepository;
    private final MemberRepository memberRepository;
    private final BadgeRepository badgeRepository;

    public MemberBadgeService(final MemberBadgeRepository memberBadgeRepository,
            final MemberRepository memberRepository, final BadgeRepository badgeRepository) {
        this.memberBadgeRepository = memberBadgeRepository;
        this.memberRepository = memberRepository;
        this.badgeRepository = badgeRepository;
    }

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

    /**
     * 멤버 아이디를 통해 조회하여 연관된 모든 멤버 뱃지를 삭제합니다.
     * @param memberId 멤버 식별아이디
     */
    @Transactional
    public void softDropMemberBadgeByMemberId(Long memberId) {
        memberBadgeRepository.deleteByMemberId(memberId);
    }

    /**
     * 해당 멤버와 연관된 활성화된 첫번째 멤버 뱃지를 조회합니다.
     * @param member 멤버
     * @param isActive 활성화 상태
     * @return {@link MemberBadge}
     */
    public MemberBadge findFirstMemberBadgeByMemberAndIsActive(Member member, boolean isActive) {
        return memberBadgeRepository.findFirstByMemberAndIsActive(member, isActive);
    }
}
