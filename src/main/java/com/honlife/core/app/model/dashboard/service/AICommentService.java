package com.honlife.core.app.model.dashboard.service;

import com.honlife.core.app.model.dashboard.domain.MemberDashboard;
import com.honlife.core.app.model.dashboard.dto.DashboardWrapperDTO;
import com.honlife.core.app.model.dashboard.dto.DayRoutineCountDTO;
import com.honlife.core.app.model.dashboard.repos.MemberDashboardRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AICommentService {

    private final MemberDashboardRepository memberDashboardRepository;
    private final MemberDashBoardAIService memberDashBoardAIService;
    private final MemberRepository memberRepository;

    public String getOrCreateAIComment(String userEmail, LocalDate startDate, DashboardWrapperDTO dashboardDTO) {

        Optional<MemberDashboard> memberDashboard = memberDashboardRepository.findByMember_EmailAndStartDate(userEmail, startDate);

        if(memberDashboard.isPresent()) {
            return memberDashboard.get().getAiComment();
        }

        return createAIComment(userEmail, startDate, dashboardDTO);

    }

    private String createAIComment(String userEmail, LocalDate startDate, DashboardWrapperDTO dashboardDTO) {
        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 포맷 지정

        for (DayRoutineCountDTO dayData : dashboardDTO.getDayRoutineCount()) {
            String formattedDate = dayData.getDate().format(dateFormatter);

            stringBuilder.append(String.format("  - %s : 총 %d개, 완료 %d개%n",
                formattedDate,
                dayData.getTotalCount(),
                dayData.getCompletedCount()
            ));
        }

        String dayRoutine = stringBuilder.toString();
        ;

        String prompt = String.format("""          
            [이번 주 루틴 데이터]
            총_루틴_개수: %d
            완료_루틴_개수: %d
            획득_포인트: %d
            가장_많이_완료한_카테고리: %s
            일별 루틴 데이터: %s
            """,
            dashboardDTO.getRoutineCount().getTotalCount(),
            dashboardDTO.getRoutineCount().getCompletedCount(),
            dashboardDTO.getTotalPoint(),
            dashboardDTO.getTop5().getFirst().getCategoryName(),
            dayRoutine
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
