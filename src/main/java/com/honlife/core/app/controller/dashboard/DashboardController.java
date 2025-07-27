package com.honlife.core.app.controller.dashboard;

import com.honlife.core.app.controller.dashboard.payload.CategoryRankResponse;
import com.honlife.core.app.controller.dashboard.payload.CategoryTotalCountResponse;
import com.honlife.core.app.controller.dashboard.payload.DayRoutineCountResponse;
import com.honlife.core.app.controller.dashboard.payload.RoutineTotalCountResponse;
import com.honlife.core.app.controller.dashboard.warpper.DashboardWrapper;
import com.honlife.core.app.model.dashboard.dto.CategoryRankDTO;
import com.honlife.core.app.model.dashboard.dto.CategoryTotalCountDTO;
import com.honlife.core.app.model.dashboard.dto.DashboardWrapperDTO;
import com.honlife.core.app.model.dashboard.dto.DayRoutineCountDTO;
import com.honlife.core.app.model.dashboard.dto.RoutineTotalCountDTO;
import com.honlife.core.app.model.dashboard.service.DashboardService;
import com.honlife.core.infra.response.CommonApiResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    /**
     * 멤버 대시보드의 데이터를 조회하는 API 입니다.
     * @param date 데이토를 조회할 해당 주에 속한 날짜
     * @param userDetails 로그인한 멤버 데이터
     * @return DashboardWrapper
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<DashboardWrapper>> getDashboardData(
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime date,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        String userEmail = userDetails.getUsername();
        // 아무 날짜나 들어왔을 때 그 주의 첫번째 요일로 만듦
        LocalDate startDate = date.toLocalDate().with(DayOfWeek.MONDAY);
        // startDate를 기준으로 7일을 더해 조회 종료일(포함x)을 구함
        LocalDate endDate = LocalDate.now();
        if(startDate.plusDays(7).isBefore(LocalDate.now()))
            endDate = startDate.plusDays(7);

        // 데이터를 가져옴

        RoutineTotalCountDTO routineTotalCountDTO = dashboardService.getRoutineTotalCount(userEmail, startDate, endDate);

        List<DayRoutineCountDTO> dayRoutineCountDTOS = dashboardService.getDayRoutineCounts(userEmail, startDate, endDate);

        List<CategoryTotalCountDTO> categoryTotalCountDTOS = dashboardService.getCategoryTotalCounts(userEmail, startDate, endDate);

        List<CategoryRankDTO> categoryRankDTOS = dashboardService.getCategoryRanks(userEmail, startDate, endDate);

        Integer totalPoint = dashboardService.getTotalPoint(userEmail, startDate, endDate);
        
        String aiComment = null;


        // response에 맞게 매핑
        DashboardWrapper wrapper = DashboardWrapper.builder()
            .routineCount(mapper.map(routineTotalCountDTO, RoutineTotalCountResponse.class))
            .dayRoutineCount(dayRoutineCountDTOS.stream().map(
                dayRoutineCountDTO-> mapper.map(dayRoutineCountDTO,DayRoutineCountResponse.class)
            ).toList())
            .categoryCount(categoryTotalCountDTOS.stream().map(
                categoryTotalCountDTO-> mapper.map(categoryTotalCountDTO,CategoryTotalCountResponse.class)
            ).toList())
            .top5(categoryRankDTOS.stream().map(
                categoryRankDTO->mapper.map(categoryRankDTO,CategoryRankResponse.class)
            ).toList())
            .totalPoint(totalPoint)
            .aiComment(aiComment)
            .build();

        return ResponseEntity.ok(CommonApiResponse.success(wrapper));
    }

}
