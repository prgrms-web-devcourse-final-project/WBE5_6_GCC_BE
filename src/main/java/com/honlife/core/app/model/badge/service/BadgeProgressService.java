package com.honlife.core.app.model.badge.service;

import com.honlife.core.app.model.badge.code.CountType;
import com.honlife.core.app.model.badge.code.ProgressType;
import com.honlife.core.app.model.badge.domain.BadgeProgress;
import com.honlife.core.app.model.badge.repos.BadgeProgressRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.service.MemberService;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeProgressService {

    private final BadgeProgressRepository badgeProgressRepository;
    private final MemberService memberService;

    /**
     * 카테고리별 루틴 완료 진행률 증가
     * @param memberId 회원 ID
     * @param categoryId 카테고리 ID
     */
    @Transactional
    public void incrementCategoryProgress(Long memberId, Long categoryId) {
        log.debug("Incrementing category progress for member: {}, category: {}", memberId, categoryId);

        BadgeProgress progress = findOrCreateCategoryProgress(memberId, categoryId);
        progress.setCountValue(progress.getCountValue() + 1);
        progress.setLastDate(LocalDate.now());

        badgeProgressRepository.save(progress);

        log.debug("Category progress updated - member: {}, category: {}, new count: {}",
            memberId, categoryId, progress.getCountValue());
    }

    /**
     * 카테고리별 루틴 완료 진행률 감소
     * @param memberId 회원 ID
     * @param categoryId 카테고리 ID
     */
    @Transactional
    public void decrementCategoryProgress(Long memberId, Long categoryId) {
        log.debug("Decrementing category progress for member: {}, category: {}", memberId, categoryId);

        Optional<BadgeProgress> progressOpt = badgeProgressRepository
            .findByMemberIdAndProgressTypeAndProgressKey(
                memberId, ProgressType.CATEGORY, categoryId.toString());

        if (progressOpt.isEmpty()) {
            log.warn("No progress found to decrement for member: {}, category: {}", memberId, categoryId);
            return;
        }

        BadgeProgress progress = progressOpt.get();

        // 0 이하로 내려가지 않도록 보호
        if (progress.getCountValue() <= 0) {
            log.warn("Progress already at 0, cannot decrement further - member: {}, category: {}",
                memberId, categoryId);
            return;
        }

        progress.setCountValue(progress.getCountValue() - 1);
        progress.setLastDate(LocalDate.now());

        badgeProgressRepository.save(progress);

        log.debug("Category progress decremented - member: {}, category: {}, new count: {}",
            memberId, categoryId, progress.getCountValue());
    }

    /**
     * 로그인 연속 진행률 업데이트
     * @param memberId 회원 ID
     */
    @Transactional
    public void updateLoginStreak(Long memberId) {
        log.debug("Updating login streak for member: {}", memberId);

        BadgeProgress progress = findOrCreateLoginProgress(memberId);
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        if (progress.getLastDate() != null && progress.getLastDate().equals(yesterday)) {
            // 연속 로그인 유지 - 카운트 증가
            progress.setCountValue(progress.getCountValue() + 1);
            log.debug("Login streak continued - member: {}, new streak: {}", memberId, progress.getCountValue());
        } else if (progress.getLastDate() != null && progress.getLastDate().equals(today)) {
            // 오늘 이미 로그인함 - 아무것도 하지 않음
            log.debug("Already logged in today - member: {}", memberId);
            return;
        } else {
            // 연속 끊어짐 또는 첫 로그인 - 1로 리셋
            progress.setCountValue(1);
            log.debug("Login streak reset - member: {}, new streak: 1", memberId);
        }

        progress.setLastDate(today);
        badgeProgressRepository.save(progress);
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
