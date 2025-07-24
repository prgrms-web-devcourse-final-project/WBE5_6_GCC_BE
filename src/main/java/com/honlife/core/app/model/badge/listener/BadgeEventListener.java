package com.honlife.core.app.model.badge.listener;

import com.honlife.core.app.model.badge.event.LoginEvent;
import com.honlife.core.app.model.badge.event.RoutineCompletedEvent;
import com.honlife.core.app.model.badge.service.BadgeProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BadgeEventListener {

    private final BadgeProgressService badgeProgressService;

    /**
     * 루틴 완료 이벤트 처리
     * @param event 루틴 완료 이벤트
     */
    @EventListener
    @Async
    public void onRoutineCompleted(RoutineCompletedEvent event) {
        try {
            log.debug("Processing routine completed event - memberId: {}, categoryId: {}",
                event.getMemberId(), event.getCategoryId());

            badgeProgressService.incrementCategoryProgress(
                event.getMemberId(),
                event.getCategoryId()
            );

            log.debug("Successfully updated badge progress for routine completion - memberId: {}, categoryId: {}",
                event.getMemberId(), event.getCategoryId());

        } catch (Exception e) {
            log.error("Failed to process routine completed event - memberId: {}, categoryId: {}, error: {}",
                event.getMemberId(), event.getCategoryId(), e.getMessage(), e);
            // 이벤트 처리 실패해도 원본 로직에는 영향 없음
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
