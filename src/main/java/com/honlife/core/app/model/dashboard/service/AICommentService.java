package com.honlife.core.app.model.dashboard.service;

import com.honlife.core.app.model.dashboard.domain.MemberDashboard;
import com.honlife.core.app.model.dashboard.dto.DashboardWrapperDTO;
import com.honlife.core.app.model.dashboard.dto.DayRoutineCountDTO;
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

    public String getOrCreateAIComment(String userEmail, LocalDate startDate, LocalDate endDate, DashboardWrapperDTO dashboardWrapperDTO) {

        Optional<MemberDashboard> memberDashboard = memberDashboardRepository.findByMember_EmailAndStartDate(userEmail, startDate);

        if(memberDashboard.isPresent()) {
            return memberDashboard.get().getAiComment();
        }

        return createAIComment(userEmail, startDate, endDate, dashboardWrapperDTO);

    }

    private String createAIComment(String userEmail, LocalDate startDate, LocalDate endDate, DashboardWrapperDTO dashboardDTO) {
        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 포맷 지정

        List<RoutineSchedule> routineSchedules = routineScheduleRepository.findAllByDateBetween(userEmail, startDate, endDate);
        
        // 루틴 데이터가 없을 경우 빠른 리턴 
        if(dashboardDTO.getRoutineCount().getTotalCount()==0) {
            return null;
        }

        // 이 데이터를 안 보내주니까 완료율 같은 걸 지멋대로 계산하길래 더 정확한 데이터를 위해 추가하였습니다.
        for (DayRoutineCountDTO dayData : dashboardDTO.getDayRoutineCount()) {
            String formattedDate = dayData.getDate().format(dateFormatter);

            stringBuilder.append(String.format("  - %s : 총 %d개, 완료 %d개%n",
                formattedDate,
                dayData.getTotalCount(),
                dayData.getCompletedCount()
            ));
        }

        String dayRoutine = stringBuilder.toString();

        for (RoutineSchedule routineSchedule : routineSchedules) {
            String formattedDate = routineSchedule.getScheduledDate().format(dateFormatter);

            stringBuilder.append(String.format("  - %s : %s 카테고리에 해당하는 %s라는 루틴 수행, 완료 여부 = %b",
                formattedDate,
                routineSchedule.getRoutine().getCategory().getName(),
                routineSchedule.getRoutine().getContent(),
                routineSchedule.getIsDone()
            ));
        }

        String prompt = createPrompt(dashboardDTO, stringBuilder, dayRoutine);

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

    private static String createPrompt(DashboardWrapperDTO dashboardDTO, StringBuilder stringBuilder,
        String dayRoutine) {
        String allRoutineData = stringBuilder.toString();

        // 혹시나 데이터가 비어있을 때를 대비
        String topCategory = "없음";
        if (dashboardDTO.getTop5() != null && !dashboardDTO.getTop5().isEmpty()) {
            topCategory = dashboardDTO.getTop5().getFirst().getCategoryName();
        }

        return String.format("""          
            [This Week's Routine Data]
            Total Routines: %d
            Completed Routines: %d
            Points Earned: %d
            Most Completed Category: %s
            Daily Routine Data: %s
            All Routine Data: %s
            """,
            dashboardDTO.getRoutineCount().getTotalCount(),
            dashboardDTO.getRoutineCount().getCompletedCount(),
            dashboardDTO.getTotalPoint(),
            topCategory,
            dayRoutine,
            allRoutineData
        );
    }
}
