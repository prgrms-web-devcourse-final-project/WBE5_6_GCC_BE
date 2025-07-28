package com.honlife.core.app.model.badge.service;

import com.honlife.core.app.model.badge.code.BadgeStatus;
import com.honlife.core.app.model.badge.code.ProgressType;
import com.honlife.core.app.model.badge.domain.Badge;
import com.honlife.core.app.model.badge.dto.BadgeDTO;
import com.honlife.core.app.model.badge.dto.BadgeRewardDTO;
import com.honlife.core.app.model.badge.dto.BadgeStatusDTO;
import com.honlife.core.app.model.badge.repos.BadgeRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.member.domain.MemberBadge;
import com.honlife.core.app.model.member.model.MemberDTO;
import com.honlife.core.app.model.member.repos.MemberBadgeRepository;
import com.honlife.core.app.model.member.service.MemberBadgeService;
import com.honlife.core.app.model.member.service.MemberPointService;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.app.model.point.code.PointLogType;
import com.honlife.core.app.model.point.dto.PointPolicyDTO;
import com.honlife.core.app.model.point.service.PointLogService;
import com.honlife.core.app.model.point.service.PointPolicyService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.error.exceptions.NotFoundException;
import com.honlife.core.infra.error.exceptions.ReferencedWarning;
import com.honlife.core.infra.response.ResponseCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final CategoryRepository categoryRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final MemberService memberService;
    private final BadgeProgressService badgeProgressService;
    private final MemberBadgeService memberBadgeService;
    private final MemberPointService memberPointService;
    private final PointPolicyService pointPolicyService;
    private final PointLogService pointLogService;

    /**
     * 모든 배지를 사용자 상태와 함께 페이지네이션으로 조회
     * @param email 사용자 이메일
     * @param pageable 페이지 정보
     * @return 페이지네이션된 배지 상태 정보
     */
    @Transactional(readOnly = true)
    public Page<BadgeStatusDTO> getAllBadgesWithStatus(String email, Pageable pageable) {
        // 1. 페이지네이션으로 활성 배지 조회
        Page<Badge> badgePage = badgeRepository.findPagedByIsActiveTrue(pageable);

        // 2. Member 조회
        MemberDTO memberDTO = memberService.findMemberByEmail(email);
        Long memberId = memberDTO.getId();

        // 3. 회원이 획득한 배지들 조회
        List<MemberBadge> memberBadges = memberBadgeRepository.findByMemberId(memberId);
        Map<Long, MemberBadge> memberBadgeMap = memberBadges.stream()
            .collect(Collectors.toMap(
                mb -> mb.getBadge().getId(),
                mb -> mb
            ));

        // 4. Page<Badge> → Page<BadgeStatusDTO> 변환
        return badgePage.map(badge -> {
            MemberBadge memberBadge = memberBadgeMap.get(badge.getId());

            // 진행률 계산
            int currentProgress = calculateBadgeProgress(badge, memberId);

            // 배지 상태 결정
            BadgeStatus status = determineBadgeStatus(badge, memberBadge, currentProgress);

            return BadgeStatusDTO.builder()
                .badgeId(badge.getId())
                .badgeKey(badge.getKey())
                .badgeName(badge.getName())
                .tier(badge.getTier())
                .message(badge.getMessage())
                .info(badge.getInfo())
                .requirement(badge.getRequirement())
                .categoryName(badge.getCategory() != null ? badge.getCategory().getName() : null)
                .status(status)
                .currentProgress(shouldShowProgress(status) ? currentProgress : null)
                .receivedDate(memberBadge != null ? memberBadge.getCreatedAt() : null)
                .isEquipped(memberBadge != null && Boolean.TRUE.equals(memberBadge.getIsEquipped()))
                .build();
        });
    }

    /**
     * 배지 상태 결정 (LOCKED, ACHIEVABLE, OWNED, EQUIPPED)
     * @param badge 배지 정보
     * @param memberBadge 회원 배지 정보 (null 가능)
     * @param currentProgress 현재 진행률
     * @return 배지 상태
     */
    private BadgeStatus determineBadgeStatus(Badge badge, MemberBadge memberBadge, int currentProgress) {
        if (memberBadge != null) {
            // 이미 획득한 배지
            return Boolean.TRUE.equals(memberBadge.getIsEquipped()) ? BadgeStatus.EQUIPPED : BadgeStatus.OWNED;
        } else {
            // 미획득 배지 - 달성 조건 확인
            return currentProgress >= badge.getRequirement() ? BadgeStatus.ACHIEVABLE : BadgeStatus.LOCKED;
        }
    }

    /**
     * 진행률을 표시해야 하는 상태인지 확인
     * @param status 배지 상태
     * @return 진행률 표시 여부
     */
    private boolean shouldShowProgress(BadgeStatus status) {
        return status == BadgeStatus.LOCKED || status == BadgeStatus.ACHIEVABLE;
    }

    /**
     * 배지 보상 수령 - 실제 구현 (도메인 분리 준수)
     * @param badgeKey 배지 key 값
     * @param email 사용자 이메일
     * @return 완료한 배지에 대한 정보 및 포인트 획득 내역 DTO
     */
    @Transactional
    public BadgeRewardDTO claimBadgeReward(String badgeKey, String email) {
        // 1. 배지 조회
        Badge badge = badgeRepository.findByKeyAndIsActiveTrue(badgeKey)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_BADGE));

        // 2. 사용자 조회
        MemberDTO memberDTO = memberService.findMemberByEmail(email);
        Long memberId = memberDTO.getId();

        // 3. 이미 획득했는지 체크
        boolean alreadyOwned = memberBadgeService.existsByMemberIdAndBadgeId(memberId, badge.getId());
        if (alreadyOwned) {
            throw new CommonException(ResponseCode.GRANT_CONFLICT_BADGE);
        }

        // 4. 달성 조건 만족하는지 체크
        int currentProgress = calculateBadgeProgress(badge, memberId);
        if (currentProgress < badge.getRequirement()) {
            throw new CommonException(ResponseCode.BAD_REQUEST); // 달성 조건 미충족
        }

        // 5. 포인트 정책 조회
        PointPolicyDTO pointPolicy = pointPolicyService.findByReferenceKey(badgeKey);

        // 6. 포인트 지급
        int pointToAdd = pointPolicy.getPoint();
        int newTotalPoint = memberPointService.addPointToMember(memberId, pointToAdd);

        // 7. 포인트 로그 기록
        pointLogService.recordPointLog(email, pointToAdd, "Badge reward: " + badge.getName(), PointLogType.GET);

        // 8. MemberBadge 생성 (배지 획득 처리)
        memberBadgeService.createMemberBadge(memberId, badge.getId());

        // 9. DTO 반환
        return BadgeRewardDTO.builder()
            .badgeId(badge.getId())
            .badgeKey(badge.getKey())
            .badgeName(badge.getName())
            .message(badge.getMessage())
            .pointAdded((long) pointToAdd)
            .totalPoint((long) newTotalPoint)
            .receivedAt(LocalDateTime.now())
            .build();
    }

    /**
     * 배지의 현재 진행률 계산
     * @param badge 배지 정보
     * @param memberId 회원 ID
     * @return 현재 진행 횟수
     */
    private int calculateBadgeProgress(Badge badge, Long memberId) {
        if (badge.getCategory() == null) {
            // 카테고리가 없는 배지는 로그인 배지로 가정
            return badgeProgressService.getCurrentProgress(
                memberId, ProgressType.LOGIN, "DAILY"
            );
        } else {
            // 카테고리가 있는 배지는 루틴 배지
            return badgeProgressService.getCurrentProgress(
                memberId, ProgressType.CATEGORY, badge.getCategory().getId().toString()
            );
        }
    }

    /**
     * 배지 장착 상태 변경
     * @param badgeKey 배지 키
     * @param email 사용자 이메일
     * @param isEquipped 장착 여부 (true: 장착, false: 해제)
     */
    @Transactional
    public void updateBadgeEquipStatus(String badgeKey, String email, boolean isEquipped) {
        // 1. 배지 조회
        Badge badge = badgeRepository.findByKeyAndIsActiveTrue(badgeKey)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_BADGE));

        // 2. 사용자 조회
        MemberDTO memberDTO = memberService.findMemberByEmail(email);
        Long memberId = memberDTO.getId();

        // 3. 해당 배지를 보유하고 있는지 확인
        MemberBadge memberBadge = memberBadgeRepository.findByMemberIdAndBadge(memberId, badge)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_BADGE));

        if (isEquipped) {
            // 장착 로직
            // 4-1. 이미 장착 중인지 확인
            if (Boolean.TRUE.equals(memberBadge.getIsEquipped())) {
                throw new CommonException(ResponseCode.BAD_REQUEST); // 이미 장착됨
            }

            // 4-2. 기존에 장착된 배지가 있다면 해제
            memberBadgeRepository.findByMemberIdAndIsEquippedTrue(memberId)
                .ifPresent(equippedBadge -> {
                    equippedBadge.setIsEquipped(false);
                    memberBadgeRepository.save(equippedBadge);
                });

            // 4-3. 새 배지 장착
            memberBadge.setIsEquipped(true);

        } else {
            // 해제 로직
            // 4-4. 현재 장착 중인지 확인
            if (!Boolean.TRUE.equals(memberBadge.getIsEquipped())) {
                throw new CommonException(ResponseCode.BAD_REQUEST); // 장착되지 않음
            }

            // 4-5. 배지 해제
            memberBadge.setIsEquipped(false);
        }

        // 5. 변경사항 저장
        memberBadgeRepository.save(memberBadge);
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
        badgeDTO.setMessage(badge.getMessage());
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
        badge.setMessage(badgeDTO.getMessage());
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
