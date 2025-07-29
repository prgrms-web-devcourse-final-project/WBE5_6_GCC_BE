package com.honlife.core.app.model.dashboard.scheduler;

import com.honlife.core.app.model.dashboard.dto.DashboardWrapperDTO;
import com.honlife.core.app.model.dashboard.service.AICommentService;
import com.honlife.core.app.model.dashboard.service.DashboardService;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class MemberDashboardScheduler {


    private final MemberRepository memberRepository;
    private final AICommentService aiCommentService;
    private final DashboardService dashboardService;

    /**
     * 일 -> 월 넘어갈 때 AI comment를 생성
     */
    @Async
    @Transactional
    @Scheduled(cron = "0 0 0 ? * MON")
    public void createAIComment() {
        log.info("주간 리포트를 위한 AI 조언을 생성 중입니다...");
        List<Member> members = memberRepository.findAllByIsActive(true);

        LocalDate endDate = LocalDate.now().minusDays(1);
        LocalDate startDate = endDate.with(DayOfWeek.MONDAY);
        // 활성화된 member들의 ai comment를 만듭니다.
        for (Member member : members) {
            DashboardWrapperDTO dashboardWrapperDTO = dashboardService.getDashboardData(member.getEmail(), startDate, endDate);
            aiCommentService.getOrCreateAIComment(member.getEmail(),startDate, endDate, dashboardWrapperDTO);
        }

    }

}
