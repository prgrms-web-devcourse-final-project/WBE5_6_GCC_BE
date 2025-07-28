package com.honlife.core.app.model.badge.listener;

import com.honlife.core.app.model.badge.event.LoginEvent;
import com.honlife.core.app.model.badge.event.RoutineProgressEvent;
import com.honlife.core.app.model.badge.service.BadgeProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class BadgeEventListener {

    private final BadgeProgressService badgeProgressService;

    /**
     * 루틴 진행률 변경 이벤트 처리 (완료/취소 통합)
     * @param event 루틴 진행률 변경 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void onRoutineProgress(RoutineProgressEvent event) {
        try {
            log.debug("Processing routine progress event after transaction commit - memberId: {}, categoryId: {}, isDone: {}",
                event.getMemberId(), event.getCategoryId(), event.getIsDone());

            if (event.getIsDone()) {
                // 루틴 완료
                badgeProgressService.incrementCategoryProgress(
                    event.getMemberId(),
                    event.getCategoryId()
                );
                log.debug("Successfully incremented badge progress - memberId: {}, categoryId: {}",
                    event.getMemberId(), event.getCategoryId());
            } else {
                // 루틴 완료 취소
                badgeProgressService.decrementCategoryProgress(
                    event.getMemberId(),
                    event.getCategoryId()
                );
                log.debug("Successfully decremented badge progress - memberId: {}, categoryId: {}",
                    event.getMemberId(), event.getCategoryId());
            }

        } catch (Exception e) {
            log.error("Failed to process routine progress event - memberId: {}, categoryId: {}, isDone: {}, error: {}",
                event.getMemberId(), event.getCategoryId(), event.getIsDone(), e.getMessage(), e);
        }
    }

    /**
     * 로그인 이벤트 처리
     * @param event 로그인 이벤트
     */
    @EventListener
    @Async
    public void onMemberLogin(LoginEvent event) {
        try {
            log.debug("Processing login event - memberId: {}", event.getMemberId());

            badgeProgressService.updateLoginStreak(event.getMemberId());

            log.debug("Successfully updated login streak for member: {}", event.getMemberId());

        } catch (Exception e) {
            log.error("Failed to process login event - memberId: {}, error: {}",
                event.getMemberId(), e.getMessage(), e);
            // 이벤트 처리 실패해도 원본 로직에는 영향 없음
        }
    }
}
