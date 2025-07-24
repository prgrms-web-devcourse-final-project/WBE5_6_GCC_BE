package com.honlife.core.app.model.badge.service;

import com.honlife.core.app.model.badge.code.ProgressType;
import com.honlife.core.app.model.badge.domain.Badge;
import com.honlife.core.app.model.badge.dto.BadgeDTO;
import com.honlife.core.app.model.badge.dto.BadgeRewardDTO;
import com.honlife.core.app.model.badge.dto.BadgeWithMemberInfoDTO;
import com.honlife.core.app.model.badge.repos.BadgeRepository;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.member.domain.MemberBadge;
import com.honlife.core.app.model.member.domain.MemberPoint;
import com.honlife.core.app.model.member.model.MemberDTO;
import com.honlife.core.app.model.member.repos.MemberBadgeRepository;
import com.honlife.core.app.model.member.repos.MemberPointRepository;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.app.model.point.code.PointLogType;
import com.honlife.core.app.model.point.domain.PointLog;
import com.honlife.core.app.model.point.domain.PointPolicy;
import com.honlife.core.app.model.point.repos.PointLogRepository;
import com.honlife.core.app.model.point.repos.PointPolicyRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.error.exceptions.NotFoundException;
import com.honlife.core.infra.error.exceptions.ReferencedWarning;
import com.honlife.core.infra.response.ResponseCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
    private final PointPolicyRepository pointPolicyRepository;
    private final MemberPointRepository memberPointRepository;
    private final PointLogRepository pointLogRepository;

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
            .message(badge.getMessage())
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
                    .message(badge.getMessage())
                    .requirement(badge.getRequirement())
                    .info(badge.getInfo())
                    .categoryName(badge.getCategory() != null ? badge.getCategory().getName() : null)
                    .isReceived(isReceived)
                    .receivedDate(isReceived ? receivedDateMap.get(badge.getId()) : null)
                    .build();
            })
            .toList();
    }

    /**
     * 배지 보상 수령 - 실제 구현
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
        boolean alreadyOwned = memberBadgeRepository.existsByMemberIdAndBadge(memberId, badge);
        if (alreadyOwned) {
            throw new CommonException(ResponseCode.GRANT_CONFLICT_BADGE);
        }

        // 4. 달성 조건 만족하는지 체크
        int currentProgress = calculateBadgeProgress(badge, memberId);
        if (currentProgress < badge.getRequirement()) {
            throw new CommonException(ResponseCode.BAD_REQUEST); // 달성 조건 미충족
        }

        // 5. 포인트 정책 조회
        PointPolicy pointPolicy = pointPolicyRepository.findByReferenceKeyAndIsActiveTrue(badgeKey)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_POLICY));

        // 6. 포인트 지급
        MemberPoint memberPoint = memberPointRepository.findByMemberId(memberId)
            .filter(mp -> Boolean.TRUE.equals(mp.getIsActive()))  // 활성화 상태 체크
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_POINT));

        int pointToAdd = pointPolicy.getPoint();
        int currentPoint = memberPoint.getPoint();
        int newTotalPoint = currentPoint + pointToAdd;

        memberPoint.setPoint(newTotalPoint);
        memberPointRepository.save(memberPoint);

        // 7. 포인트 로그 기록
        PointLog pointLog = PointLog.builder()
            .member(memberService.getMemberByEmail(email))
            .type(PointLogType.GET)
            .point(pointToAdd)
            .reason("Badge reward: " + badge.getName())
            .time(LocalDateTime.now())
            .build();
        pointLogRepository.save(pointLog);

        // 8. MemberBadge 생성 (배지 획득 처리)
        MemberBadge memberBadge = MemberBadge.builder()
            .member(memberService.getMemberByEmail(email))
            .badge(badge)
            .isEquipped(false)  // 기본값: 미장착
            .build();
        memberBadgeRepository.save(memberBadge);

        // 9. DTO 반환
        return BadgeRewardDTO.builder()
            .badgeId(badge.getId())
            .badgeKey(badge.getKey())
            .badgeName(badge.getName())
            .message(badge.getMessage())  // 축하 메시지
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
