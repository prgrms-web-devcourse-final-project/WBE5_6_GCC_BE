package com.honlife.core.app.model.badge.service;

import com.honlife.core.app.model.badge.code.CountType;
import com.honlife.core.app.model.badge.code.ProgressType;
import com.honlife.core.app.model.badge.domain.Badge;
import com.honlife.core.app.model.badge.domain.BadgeProgress;
import com.honlife.core.app.model.badge.repos.BadgeProgressRepository;
import com.honlife.core.app.model.badge.repos.BadgeRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.service.MemberBadgeService;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.app.model.notification.code.NotificationType;
import com.honlife.core.app.model.notification.service.NotifyListService;
import com.honlife.core.app.model.notification.service.NotifyPublisher;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BadgeProgressService {

    private final BadgeProgressRepository badgeProgressRepository;
    private final BadgeRepository badgeRepository;
    private final MemberBadgeService memberBadgeService;
    private final NotifyListService notifyListService;
    private final MemberService memberService;
    private final NotifyPublisher notifyPublisher;

    /**
     * 카테고리별 루틴 완료 진행률 증가
     * @param memberId 회원 ID
     * @param categoryId 카테고리 ID
     */
    @Transactional
    public void incrementCategoryProgress(Long memberId, Long categoryId) {
        BadgeProgress progress = findOrCreateCategoryProgress(memberId, categoryId);
        int oldCount = progress.getCountValue();
        int newCount = oldCount + 1;

        progress.setCountValue(newCount);
        progress.setLastDate(LocalDate.now());
        badgeProgressRepository.save(progress);

        // 🔔 배지 달성 체크 및 알림
        checkAndNotifyBadgeAchievement(memberId, categoryId, oldCount, newCount);
    }

    /**
     * 카테고리별 루틴 완료 진행률 감소
     * @param memberId 회원 ID
     * @param categoryId 카테고리 ID
     */
    @Transactional
    public void decrementCategoryProgress(Long memberId, Long categoryId) {

        Optional<BadgeProgress> progressOpt = badgeProgressRepository
            .findByMemberIdAndProgressTypeAndProgressKey(
                memberId, ProgressType.CATEGORY, categoryId.toString());

        if (progressOpt.isEmpty()) return;

        BadgeProgress progress = progressOpt.get();

        // 0 이하로 내려가지 않도록 보호
        if (progress.getCountValue() <= 0) return;

        progress.setCountValue(progress.getCountValue() - 1);
        progress.setLastDate(LocalDate.now());

        badgeProgressRepository.save(progress);
    }

    /**
     * 로그인 연속 진행률 업데이트
     * @param memberId 회원 ID
     */
    @Transactional
    public void updateLoginStreak(Long memberId) {

        BadgeProgress progress = findOrCreateLoginProgress(memberId);
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        int oldStreak = progress.getCountValue();

        if (progress.getLastDate() != null && progress.getLastDate().equals(yesterday)) {
            // 연속 로그인 유지 - 카운트 증가
            progress.setCountValue(progress.getCountValue() + 1);
        } else if (progress.getLastDate() != null && progress.getLastDate().equals(today)) {
            // 오늘 이미 로그인함 - 아무것도 하지 않음
            return;
        } else {
            // 연속 끊어짐 또는 첫 로그인 - 1로 리셋
            progress.setCountValue(1);
        }

        progress.setLastDate(today);
        badgeProgressRepository.save(progress);

        // 🔔 로그인 배지 달성 체크 및 알림
        checkAndNotifyLoginBadgeAchievement(memberId, oldStreak, progress.getCountValue());
    }

    /**
     * 특정 진행률 조회
     * @param memberId 회원 ID
     * @param progressType 진행률 타입
     * @param progressKey 세부 식별자
     * @return 현재 진행 횟수 (없으면 0)
     */
    @Transactional(readOnly = true)
    public int getCurrentProgress(Long memberId, ProgressType progressType, String progressKey) {
        return badgeProgressRepository
            .findByMemberIdAndProgressTypeAndProgressKey(memberId, progressType, progressKey)
            .map(BadgeProgress::getCountValue)
            .orElse(0);
    }

    /**
     * 카테고리 배지 달성 체크 및 알림
     */
    private void checkAndNotifyBadgeAchievement(Long memberId, Long categoryId, int oldCount, int newCount) {
        // 해당 카테고리의 배지들 조회
        List<Badge> categoryBadges = badgeRepository.findByCategoryIdAndIsActiveTrue(categoryId);

        for (Badge badge : categoryBadges) {
            // 이전에는 달성 못했는데 이번에 달성한 경우
            if (oldCount < badge.getRequirement() && newCount >= badge.getRequirement()) {
                // 이미 획득한 배지인지 확인
                boolean alreadyOwned = memberBadgeService.existsByMemberIdAndBadgeId(memberId, badge.getId());
                if (!alreadyOwned) {
                    // 🔔 배지 달성 알림 발송
                    String userEmail = memberService.get(memberId).getEmail();
                    String title = badge.getName() + " 배지 달성";

                    notifyPublisher.saveNotifyAndSendSse(userEmail, title, NotificationType.BADGE);
                }
            }
        }
    }

    /**
     * 로그인 배지 달성 체크 및 알림
     */
    private void checkAndNotifyLoginBadgeAchievement(Long memberId, int oldStreak, int newStreak) {
        // 로그인 배지들 조회 (category_id가 null)
        List<Badge> loginBadges = badgeRepository.findByCategoryIsNullAndIsActiveTrue();

        for (Badge badge : loginBadges) {
            // 이전에는 달성 못했는데 이번에 달성한 경우
            if (oldStreak < badge.getRequirement() && newStreak >= badge.getRequirement()) {
                // 이미 획득한 배지인지 확인
                boolean alreadyOwned = memberBadgeService.existsByMemberIdAndBadgeId(memberId, badge.getId());
                if (!alreadyOwned) {
                    // 🔔 로그인 배지 달성 알림 발송
                    String userEmail = memberService.get(memberId).getEmail();
                    String title = badge.getName() + " 배지 달성";

                    notifyPublisher.saveNotifyAndSendSse(userEmail, title, NotificationType.BADGE);
                }
            }
        }
    }

    /**
     * 카테고리 진행률 조회 또는 생성
     */
    private BadgeProgress findOrCreateCategoryProgress(Long memberId, Long categoryId) {
        return badgeProgressRepository
            .findByMemberIdAndProgressTypeAndProgressKey(
                memberId, ProgressType.CATEGORY, categoryId.toString())
            .orElseGet(() -> createCategoryProgress(memberId, categoryId));
    }

    /**
     * 로그인 진행률 조회 또는 생성
     */
    private BadgeProgress findOrCreateLoginProgress(Long memberId) {
        return badgeProgressRepository
            .findByMemberIdAndProgressTypeAndProgressKey(
                memberId, ProgressType.LOGIN, "DAILY")
            .orElseGet(() -> createLoginProgress(memberId));
    }

    /**
     * 새 카테고리 진행률 생성
     */
    private BadgeProgress createCategoryProgress(Long memberId, Long categoryId) {
        Member member = memberService.getMemberByEmail(
            memberService.get(memberId).getEmail());

        BadgeProgress progress = BadgeProgress.builder()
            .member(member)
            .progressType(ProgressType.CATEGORY)
            .progressKey(categoryId.toString())
            .countType(CountType.CUMULATIVE)
            .countValue(0)
            .lastDate(LocalDate.now())
            .build();

        return badgeProgressRepository.save(progress);
    }

    /**
     * 새 로그인 진행률 생성
     */
    private BadgeProgress createLoginProgress(Long memberId) {
        Member member = memberService.getMemberByEmail(
            memberService.get(memberId).getEmail());

        BadgeProgress progress = BadgeProgress.builder()
            .member(member)
            .progressType(ProgressType.LOGIN)
            .progressKey("DAILY")
            .countType(CountType.STREAK)
            .countValue(0)
            .lastDate(LocalDate.now())
            .build();

        return badgeProgressRepository.save(progress);
    }
}
