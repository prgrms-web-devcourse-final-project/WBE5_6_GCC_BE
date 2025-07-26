package com.honlife.core.app.controller.dashboard;

import com.honlife.core.app.controller.dashboard.payload.CategoryRankResponse;
import com.honlife.core.app.controller.dashboard.payload.CategoryTotalCountResponse;
import com.honlife.core.app.controller.dashboard.payload.DayRoutineCountResponse;
import com.honlife.core.app.controller.dashboard.payload.RoutineTotalCountResponse;
import com.honlife.core.app.controller.dashboard.warpper.DashboardWrapper;
import com.honlife.core.app.model.dashboard.dto.DashboardWrapperDTO;
import com.honlife.core.app.model.dashboard.service.DashboardService;
import com.honlife.core.infra.response.CommonApiResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/members/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
public class DashboardController {

    private final DashboardService dashboardService;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<CommonApiResponse<DashboardWrapper>> getDashboardData(
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime startDate,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        String userEmail = userDetails.getUsername();

        DashboardWrapperDTO dashboardDTO = dashboardService.getDashBoardData(userEmail, startDate);

        DashboardWrapper wrapper = DashboardWrapper.builder()
            .routineCount(mapper.map(dashboardDTO, RoutineTotalCountResponse.class))
            .dayRoutineCount(dashboardDTO.getDayRoutineCount().stream().map(
                dayRoutineCountDTO-> mapper.map(dayRoutineCountDTO,DayRoutineCountResponse.class)
            ).toList())
            .categoryCount(dashboardDTO.getCategoryCount().stream().map(
                categoryTotalCountDTO-> mapper.map(categoryTotalCountDTO,CategoryTotalCountResponse.class)
            ).toList())
            .top5(dashboardDTO.getTop5().stream().map(
                categoryRankDTO->mapper.map(categoryRankDTO,CategoryRankResponse.class)
            ).toList())
            .totalPoint(dashboardDTO.getTotalPoint())
            .aiComment(dashboardDTO.getAiComment())
            .build();

        return ResponseEntity.ok(CommonApiResponse.success(wrapper));
    }

}
