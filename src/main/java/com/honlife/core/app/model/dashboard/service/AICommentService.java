package com.honlife.core.app.model.dashboard.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.honlife.core.app.model.dashboard.domain.MemberDashboard;
import com.honlife.core.app.model.dashboard.dto.DashboardWrapperDTO;
import com.honlife.core.app.model.dashboard.dto.RoutineJsonDTO;
import com.honlife.core.app.model.dashboard.repos.MemberDashboardRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.routine.dto.RoutineSummaryDTO;
import com.honlife.core.app.model.routine.service.RoutineScheduleService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.gson.LocalDateSerializer;
import com.honlife.core.infra.gson.LocalDateTimeSerializer;
import com.honlife.core.infra.response.ResponseCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final RoutineScheduleService routineScheduleService;

    public String getOrCreateAIComment(String userEmail, LocalDate startDate, LocalDate endDate, DashboardWrapperDTO dashboardWrapperDTO) {

        Optional<MemberDashboard> memberDashboard = memberDashboardRepository.findByMember_EmailAndStartDate(userEmail, startDate);

        if(memberDashboard.isPresent()) {
            return memberDashboard.get().getAiComment();
        }

        return createAIComment(userEmail, startDate, endDate, dashboardWrapperDTO);

    }

    private String createAIComment(String userEmail, LocalDate startDate, LocalDate endDate, DashboardWrapperDTO dashboardDTO) {

        List<RoutineSummaryDTO> routineSummary = routineScheduleService.getRoutineSummary(userEmail, startDate, endDate);


        // 루틴 데이터가 없을 경우 빠른 리턴 
        if(dashboardDTO.getRoutineCount().getTotalCount()==0) {
            return null;
        }

        String prompt = createPrompt(dashboardDTO, routineSummary);

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

    /**
     * AI 에게 넘겨줄 json data를 생성합니다.
     * @param dashboardDTO 통계 데이터
     * @param routineSchedules 매일 루틴 데이터
     * @return String
     */
    private String createPrompt(DashboardWrapperDTO dashboardDTO, List<RoutineSummaryDTO> routineSchedules) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());

        Gson gson = gsonBuilder.setPrettyPrinting().create();

        RoutineJsonDTO data = new RoutineJsonDTO(dashboardDTO, routineSchedules);

        return gson.toJson(data);
    }
}
