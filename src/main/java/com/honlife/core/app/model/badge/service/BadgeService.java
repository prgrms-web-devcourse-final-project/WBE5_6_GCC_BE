package com.honlife.core.app.model.badge.service;

import com.honlife.core.app.model.badge.domain.Badge;
import com.honlife.core.app.model.badge.dto.BadgeDTO;
import com.honlife.core.app.model.badge.dto.BadgeWithMemberInfoDTO;
import com.honlife.core.app.model.badge.repos.BadgeRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.member.domain.MemberBadge;
import com.honlife.core.app.model.member.model.MemberDTO;
import com.honlife.core.app.model.member.repos.MemberBadgeRepository;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.infra.error.exceptions.NotFoundException;
import com.honlife.core.infra.error.exceptions.ReferencedWarning;
import com.honlife.core.infra.response.ResponseCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final CategoryRepository categoryRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final MemberService memberService;

    // === 기존 CRUD 메서드들 ===

    public List<BadgeDTO> findAll() {
        final List<Badge> badges = badgeRepository.findAll(Sort.by("id"));
        return badges.stream()
            .map(badge -> mapToDTO(badge, new BadgeDTO()))
            .toList();
    }

    public BadgeDTO get(final Long id) {
        return badgeRepository.findById(id)
            .map(badge -> mapToDTO(badge, new BadgeDTO()))
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_BADGE));
    }

    public Long create(final BadgeDTO badgeDTO) {
        final Badge badge = new Badge();
        mapToEntity(badgeDTO, badge);
        return badgeRepository.save(badge).getId();
    }

    public void update(final Long id, final BadgeDTO badgeDTO) {
        final Badge badge = badgeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_BADGE));
        mapToEntity(badgeDTO, badge);
        badgeRepository.save(badge);
    }

    public void delete(final Long id) {
        badgeRepository.deleteById(id);
    }

    // === 사용자별 조회 메서드들 ===

    /**
     * 특정 배지를 사용자 정보와 함께 조회
     * @param badgeKey 배지 키
     * @param email 사용자 이메일
     * @return 배지 정보 + 사용자 획득 여부, 배지가 없으면 null 반환
     */
    @Transactional(readOnly = true)
    public BadgeWithMemberInfoDTO getBadgeWithMemberInfo(String badgeKey, String email) {
        // 1. Badge 조회 (없으면 null 반환)
        Badge badge = badgeRepository.findByKeyAndIsActiveTrue(badgeKey).orElse(null);
        if (badge == null) return null;

        // 2. Member 조회
        MemberDTO memberDTO = memberService.findMemberByEmail(email);

        // 3. 사용자 배지 획득 여부 확인
        MemberBadge memberBadge = memberBadgeRepository.findByMemberIdAndBadge(memberDTO.getId(), badge)
            .orElse(null);

        // 4. BadgeWithMemberInfoDTO 생성
        return BadgeWithMemberInfoDTO.builder()
            .badgeId(badge.getId())
            .badgeKey(badge.getKey())
            .badgeName(badge.getName())
            .tier(badge.getTier())
            .how(badge.getHow())
            .requirement(badge.getRequirement())
            .info(badge.getInfo())
            .categoryName(badge.getCategory() != null ? badge.getCategory().getName() : null)
            .isReceived(memberBadge != null)
            .receivedDate(memberBadge != null ? memberBadge.getCreatedAt() : null)
            .build();
    }

    /**
     * 모든 배지를 사용자 정보와 함께 조회
     * @param email 사용자 이메일
     * @return 모든 배지 정보 + 사용자 획득 여부 리스트
     */
    @Transactional(readOnly = true)
    public List<BadgeWithMemberInfoDTO> getAllBadgesWithMemberInfo(String email) {
        // 1. 모든 활성 배지 조회
        List<Badge> badges = badgeRepository.findAllByIsActiveTrue();

        // 2. Member 조회
        MemberDTO memberDTO = memberService.findMemberByEmail(email);

        // 3. 회원이 획득한 배지들 조회
        List<MemberBadge> memberBadges = memberBadgeRepository.findByMemberId(memberDTO.getId());
        Set<Long> receivedBadgeIds = memberBadges.stream()
            .map(mb -> mb.getBadge().getId())
            .collect(Collectors.toSet());

        // 4. 획득 일시 맵 생성
        Map<Long, LocalDateTime> receivedDateMap = memberBadges.stream()
            .collect(Collectors.toMap(
                mb -> mb.getBadge().getId(),
                MemberBadge::getCreatedAt
            ));

        // 5. DTO 변환
        return badges.stream()
            .map(badge -> {
                boolean isReceived = receivedBadgeIds.contains(badge.getId());

                return BadgeWithMemberInfoDTO.builder()
                    .badgeId(badge.getId())
                    .badgeKey(badge.getKey())
                    .badgeName(badge.getName())
                    .tier(badge.getTier())
                    .how(badge.getHow())
                    .requirement(badge.getRequirement())
                    .info(badge.getInfo())
                    .categoryName(badge.getCategory() != null ? badge.getCategory().getName() : null)
                    .isReceived(isReceived)
                    .receivedDate(isReceived ? receivedDateMap.get(badge.getId()) : null)
                    .build();
            })
            .toList();
    }

    // === 기존 매핑 메서드들 ===

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
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_CATEGORY));
        badge.setCategory(category);
        return badge;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Badge badge = badgeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_BADGE));
        final MemberBadge badgeMemberBadge = memberBadgeRepository.findFirstByBadge(badge);
        if (badgeMemberBadge != null) {
            referencedWarning.setKey("badge.memberBadge.badge.referenced");
            referencedWarning.addParam(badgeMemberBadge.getId());
            return referencedWarning;
        }
        return null;
    }

}
