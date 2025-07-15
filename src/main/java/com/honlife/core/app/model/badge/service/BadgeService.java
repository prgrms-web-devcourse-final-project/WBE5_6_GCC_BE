package com.honlife.core.app.model.badge.service;

import com.honlife.core.app.model.badge.dto.BadgeWithMemberInfoDTO;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.badge.domain.Badge;
import com.honlife.core.app.model.badge.dto.BadgeDTO;
import com.honlife.core.app.model.badge.repos.BadgeRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.member.domain.MemberBadge;
import com.honlife.core.app.model.member.repos.MemberBadgeRepository;
import com.honlife.core.infra.util.NotFoundException;
import com.honlife.core.infra.util.ReferencedWarning;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final CategoryRepository categoryRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final MemberRepository memberRepository;

    public BadgeService(final BadgeRepository badgeRepository,
        final CategoryRepository categoryRepository,
        final MemberBadgeRepository memberBadgeRepository,
        final MemberRepository memberRepository) {
        this.badgeRepository = badgeRepository;
        this.categoryRepository = categoryRepository;
        this.memberBadgeRepository = memberBadgeRepository;
        this.memberRepository = memberRepository;
    }

    public List<BadgeDTO> findAll() {
        final List<Badge> badges = badgeRepository.findAll(Sort.by("id"));
        return badges.stream()
            .map(badge -> mapToDTO(badge, new BadgeDTO()))
            .toList();
    }

    public BadgeDTO get(final Long id) {
        return badgeRepository.findById(id)
            .map(badge -> mapToDTO(badge, new BadgeDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public Long create(final BadgeDTO badgeDTO) {
        final Badge badge = new Badge();
        mapToEntity(badgeDTO, badge);
        return badgeRepository.save(badge).getId();
    }

    public void update(final Long id, final BadgeDTO badgeDTO) {
        final Badge badge = badgeRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(badgeDTO, badge);
        badgeRepository.save(badge);
    }

    public void delete(final Long id) {
        badgeRepository.deleteById(id);
    }

    /**
     * 특정 배지를 사용자 정보와 함께 조회
     * @param badgeKey 배지 키
     * @param memberId 회원 ID
     * @return 배지 정보 + 사용자 획득 여부
     */
    @Transactional(readOnly = true)
    public BadgeWithMemberInfoDTO getBadgeWithMemberInfo(String badgeKey, Long memberId) {
        // 1. 배지 조회
        Badge badge = badgeRepository.findByKeyAndIsActiveTrue(badgeKey)
            .orElseThrow(() -> new NotFoundException("Badge not found with key: " + badgeKey));

        // 2. 사용자 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("Member not found"));

        // 3. 사용자 배지 획득 여부 확인
        MemberBadge memberBadge = memberBadgeRepository.findByMemberAndBadge(member, badge)
            .orElse(null);

        // 4. BadgeDTO 변환
        BadgeDTO badgeDTO = mapToDTO(badge, new BadgeDTO());

        // 5. BadgeWithMemberInfoDTO 생성
        return BadgeWithMemberInfoDTO.builder()
            .badge(badgeDTO)
            .categoryName(badge.getCategory() != null ? badge.getCategory().getName() : null)
            .isReceived(memberBadge != null)
            .receivedDate(memberBadge != null ? memberBadge.getCreatedAt() : null)
            .build();
    }

    /**
     * 모든 배지를 사용자 정보와 함께 조회
     * @param memberId 회원 ID
     * @return 모든 배지 정보 + 사용자 획득 여부 리스트
     */
    @Transactional(readOnly = true)
    public List<BadgeWithMemberInfoDTO> getAllBadgesWithMemberInfo(Long memberId) {
        // 1. 모든 활성 배지 조회
        List<Badge> badges = badgeRepository.findAllByIsActiveTrue();

        // 2. 회원 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("Member not found"));

        // 3. 회원이 획득한 배지들 조회
        List<MemberBadge> memberBadges = memberBadgeRepository.findByMember(member);
        Set<Long> receivedBadgeIds = memberBadges.stream()
            .map(mb -> mb.getBadge().getId())
            .collect(Collectors.toSet());

        // 4. 획득 일시 맵 생성 (성능 최적화)
        Map<Long, LocalDateTime> receivedDateMap = memberBadges.stream()
            .collect(Collectors.toMap(
                mb -> mb.getBadge().getId(),
                MemberBadge::getCreatedAt
            ));

        // 5. DTO 변환
        return badges.stream()
            .map(badge -> {
                BadgeDTO badgeDTO = mapToDTO(badge, new BadgeDTO());
                boolean isReceived = receivedBadgeIds.contains(badge.getId());

                return BadgeWithMemberInfoDTO.builder()
                    .badge(badgeDTO)
                    .categoryName(badge.getCategory() != null ? badge.getCategory().getName() : null)
                    .isReceived(isReceived)
                    .receivedDate(isReceived ? receivedDateMap.get(badge.getId()) : null)
                    .build();
            })
            .toList();
    }

    private BadgeDTO mapToDTO(final Badge badge, final BadgeDTO badgeDTO) {
        badgeDTO.setCreatedAt(badge.getCreatedAt());
        badgeDTO.setUpdatedAt(badge.getUpdatedAt());
        badgeDTO.setIsActive(badge.getIsActive());
        badgeDTO.setId(badge.getId());
        badgeDTO.setKey(badge.getKey());
        badgeDTO.setName(badge.getName());
        badgeDTO.setTier(badge.getTier());
        badgeDTO.setHow(badge.getHow());
        badgeDTO.setRequirement(badge.getRequirement());
        badgeDTO.setInfo(badge.getInfo());
        badgeDTO.setCategory(badge.getCategory() == null ? null : badge.getCategory().getId());
        return badgeDTO;
    }

    private Badge mapToEntity(final BadgeDTO badgeDTO, final Badge badge) {
        badge.setCreatedAt(badgeDTO.getCreatedAt());
        badge.setUpdatedAt(badgeDTO.getUpdatedAt());
        badge.setIsActive(badgeDTO.getIsActive());
        badge.setKey(badgeDTO.getKey());
        badge.setName(badgeDTO.getName());
        badge.setTier(badgeDTO.getTier());
        badge.setHow(badgeDTO.getHow());
        badge.setRequirement(badgeDTO.getRequirement());
        badge.setInfo(badgeDTO.getInfo());
        final Category category = badgeDTO.getCategory() == null ? null : categoryRepository.findById(badgeDTO.getCategory())
            .orElseThrow(() -> new NotFoundException("category not found"));
        badge.setCategory(category);
        return badge;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Badge badge = badgeRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        final MemberBadge badgeMemberBadge = memberBadgeRepository.findFirstByBadge(badge);
        if (badgeMemberBadge != null) {
            referencedWarning.setKey("badge.memberBadge.badge.referenced");
            referencedWarning.addParam(badgeMemberBadge.getId());
            return referencedWarning;
        }
        return null;
    }

}
