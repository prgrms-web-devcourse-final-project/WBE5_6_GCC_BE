package com.honlife.core.app.model.dashboard.scheduler;

import com.honlife.core.app.model.dashboard.service.DashboardService;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final DashboardService dashboardService;

    /**
     * 일 -> 월 넘어갈 때 AI comment를 생성
     */
    @Transactional
    @Scheduled(cron = "0 0 0 ? * MON")
    public void createAIComment() {
        log.info("주간 리포트를 위한 AI 조언을 생성 중입니다...");
        List<Member> members = memberRepository.findAllByIsActive(true);

        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        // 활성화된 member들의 ai comment를 만듭니다.
        for (Member member : members) {
            dashboardService.getDashBoardData(member.getEmail(), yesterday);
        }

    }

}
