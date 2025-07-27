package com.honlife.core.app.model.dashboard.service;

import com.honlife.core.app.model.dashboard.domain.MemberDashboard;
import com.honlife.core.app.model.dashboard.repos.MemberDashboardRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AICommentService {

    private final MemberDashboardRepository memberDashboardRepository;
    private final MemberDashBoardAIService memberDashBoardAIService;
    private final MemberRepository memberRepository;
    private final RoutineScheduleRepository routineScheduleRepository;

    public String getOrCreateAIComment(String userEmail, LocalDate startDate, LocalDate endDate) {

        Optional<MemberDashboard> memberDashboard = memberDashboardRepository.findByMember_EmailAndStartDate(userEmail, startDate);

        if(memberDashboard.isPresent()) {
            return memberDashboard.get().getAiComment();
        }

        return createAIComment(userEmail, startDate, endDate);

    }

    private String createAIComment(String userEmail, LocalDate startDate, LocalDate endDate) {
        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 포맷 지정

        List<RoutineSchedule> routineSchedules = routineScheduleRepository.findAllByDateBetween(userEmail, startDate, endDate);

        for (RoutineSchedule routineSchedule : routineSchedules) {
            String formattedDate = routineSchedule.getDate().format(dateFormatter);

            stringBuilder.append(String.format("  - %s : %s 카테고리에 해당하는 %s라는 루틴 수행, 완료 여부 = %b",
                formattedDate,
                routineSchedule.getRoutine().getCategory().getName(),
                routineSchedule.getRoutine().getContent(),
                routineSchedule.getIsDone()
            ));
        }

        String allRoutineData = stringBuilder.toString();
        ;

        String prompt = String.format("""          
            [이번 주 루틴 데이터]
            모든 루틴 데이터: %s
            """,
            allRoutineData
        );

        String aiComment=memberDashBoardAIService.chat(prompt);

        MemberDashboard savedDashboard =  MemberDashboard.builder()
            .member(memberRepository.findByEmailAndIsActive(userEmail, true).orElseThrow(()->new CommonException(
                ResponseCode.NOT_FOUND_MEMBER)))
            .startDate(startDate)
            .aiComment(aiComment)
            .build();

        memberDashboardRepository.save(savedDashboard);
        return aiComment;
    }
}
