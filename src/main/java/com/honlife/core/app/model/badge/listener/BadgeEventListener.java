package com.honlife.core.app.model.badge.listener;

import com.honlife.core.app.model.badge.event.LoginEvent;
import com.honlife.core.app.model.badge.service.BadgeProgressService;
import com.honlife.core.app.model.category.service.CategoryService;
import com.honlife.core.app.model.routine.dto.RoutineScheduleInfo;
import com.honlife.core.app.model.routine.service.RoutineScheduleService;
import com.honlife.core.infra.event.CommonEvent;
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
    private final RoutineScheduleService routineScheduleService;
    private final CategoryService categoryService;

    /**
     * 루틴 진행률 변경 이벤트 처리 (완료/취소 통합)
     * CommonEvent를 통해 루틴 완료/취소 시 배지 진행률 업데이트
     *
     * @param event 루틴 진행률 변경 이벤트 (CommonEvent)
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void onRoutineProgress(CommonEvent event) {

        log.info("🚀 onRoutineProgress 호출됨! - routineScheduleId: {}", event.getRoutineScheduleId());

        try {
            log.debug("Processing routine progress event after transaction commit - routineScheduleId: {}, isDone: {}",
                event.getRoutineScheduleId(), event.getIsDone());

            // 1. 루틴 스케줄 정보 조회 (Service를 통한 안전한 접근)
            RoutineScheduleInfo scheduleInfo = routineScheduleService
                .getRoutineScheduleInfoForBadge(event.getRoutineScheduleId());

            if (scheduleInfo == null) {
                log.warn("RoutineSchedule not found - routineScheduleId: {}", event.getRoutineScheduleId());
                return;
            }

            // 2. 상위 카테고리 찾기 (SUB → 부모, DEFAULT/MAJOR → 자기 자신)
            Long topLevelCategoryId = categoryService.findTopLevelCategoryIdForBadge(scheduleInfo.getCategoryId());

            // 3. 배지 진행률 업데이트
            if (event.getIsDone()) {
                // 루틴 완료
                badgeProgressService.incrementCategoryProgress(scheduleInfo.getMemberId(), topLevelCategoryId);
                log.debug("Successfully incremented badge progress - memberId: {}, categoryId: {}",
                    scheduleInfo.getMemberId(), topLevelCategoryId);
            } else {
                // 루틴 완료 취소
                badgeProgressService.decrementCategoryProgress(scheduleInfo.getMemberId(), topLevelCategoryId);
                log.debug("Successfully decremented badge progress - memberId: {}, categoryId: {}",
                    scheduleInfo.getMemberId(), topLevelCategoryId);
            }

        } catch (Exception e) {
            log.error("Failed to process routine progress event - routineScheduleId: {}, isDone: {}, error: {}",
                event.getRoutineScheduleId(), event.getIsDone(), e.getMessage(), e);
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
