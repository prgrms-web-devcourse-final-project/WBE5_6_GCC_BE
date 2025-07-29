package com.honlife.core.app.model.badge.listener;

import com.honlife.core.app.model.badge.service.BadgeProgressService;
import com.honlife.core.app.model.category.service.CategoryService;
import com.honlife.core.app.model.member.model.MemberDTO;
import com.honlife.core.app.model.member.service.MemberService;
import com.honlife.core.app.model.routine.dto.RoutineScheduleInfo;
import com.honlife.core.app.model.routine.service.RoutineScheduleService;
import com.honlife.core.infra.event.CommonEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BadgeEventListener {

    private final BadgeProgressService badgeProgressService;
    private final RoutineScheduleService routineScheduleService;
    private final CategoryService categoryService;
    private final MemberService memberService;

    /**
     * 루틴 진행률 변경 이벤트 처리 (완료/취소 통합)
     * CommonEvent를 통해 루틴 완료/취소 시 배지 진행률 업데이트
     *
     * @param event 루틴 진행률 변경 이벤트 (CommonEvent)
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void onRoutineProgress(CommonEvent event) {

        if (event.getRoutineScheduleId() == null) return;

        // 1. 루틴 스케줄 정보 조회 (Service를 통한 안전한 접근)
        RoutineScheduleInfo scheduleInfo = routineScheduleService
            .getRoutineScheduleInfoForBadge(event.getRoutineScheduleId());

        if (scheduleInfo == null) return;

        // 2. 상위 카테고리 찾기 (SUB → 부모, DEFAULT/MAJOR → 자기 자신)
        Long topLevelCategoryId = categoryService.findTopLevelCategoryIdForBadge(scheduleInfo.getCategoryId());

        // 3. 배지 진행률 업데이트
        if (event.getIsDone()) {
            // 루틴 완료
            badgeProgressService.incrementCategoryProgress(scheduleInfo.getMemberId(), topLevelCategoryId);

        } else {
            // 루틴 완료 취소
            badgeProgressService.decrementCategoryProgress(scheduleInfo.getMemberId(), topLevelCategoryId);

        }
    }

    /**
     * 로그인 이벤트 처리
     * @param event 로그인 이벤트
     */
    @EventListener
    @Async
    public void onMemberLogin(CommonEvent event) {

        if (event.getRoutineScheduleId() != null) return;

        MemberDTO memberDTO = memberService.findMemberByEmail(event.getMemberEmail());
        badgeProgressService.updateLoginStreak(memberDTO.getId());

    }
}
