package com.honlife.core.app.controller.dashboard;

import com.honlife.core.app.controller.dashboard.payload.CategoryRankResponse;
import com.honlife.core.app.controller.dashboard.payload.CategoryTotalCountResponse;
import com.honlife.core.app.controller.dashboard.payload.DayRoutineCountResponse;
import com.honlife.core.app.controller.dashboard.payload.RoutineTotalCountResponse;
import com.honlife.core.app.controller.dashboard.wrapper.DashboardWrapper;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "[회원] 주간 리포트", description = "주간 리포트 관련 api 입니다.")
@RequestMapping(value = "/api/v1/members/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
public class DashboardController {

    private final ModelMapper mapper;

    /**
     * 멤버 대시보드의 데이터를 조회하는 API 입니다.
     * @param date 데이토를 조회할 해당 주에 속한 날짜
     * @param userDetails 로그인한 멤버 데이터
     * @return DashboardWrapper
     */
    @GetMapping
    @Operation(summary = "주간 리포트 조회", description = "특정 날짜를 입력시 해당 날짜가 속하는 주의 리포트 정보를 조회할 수 있습니다.")
    public ResponseEntity<CommonApiResponse<DashboardWrapper>> getDashboardData(
        @Parameter(description = "조회할 날짜", example = "2025-07-06T00:00:00")
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime date,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        if(date.isBefore(LocalDateTime.parse("2025-07-01T00:00:00")) || date.isAfter(LocalDateTime.parse("2025-07-06T23:59:59"))){
            return ResponseEntity.ok(CommonApiResponse.success(new DashboardWrapper(new RoutineTotalCountResponse(0L,0L), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), null, null)));
        }

        // 데이터를 가져옴
        RoutineTotalCountResponse routineTotalCount = RoutineTotalCountResponse.builder()
            .totalCount(28L)
            .completedCount(20L)
            .build();

        List<DayRoutineCountResponse> dayRoutineCount = new ArrayList<>();

        dayRoutineCount.add(DayRoutineCountResponse.builder()
            .date(LocalDateTime.parse("2025-07-01T00:00:00"))
            .completionRate(60d)
            .build());
        dayRoutineCount.add(DayRoutineCountResponse.builder()
            .date(LocalDateTime.parse("2025-07-02T00:00:00"))
            .completionRate(100d)
            .build());
        dayRoutineCount.add(DayRoutineCountResponse.builder()
            .date(LocalDateTime.parse("2025-07-03T00:00:00"))
            .completionRate(50d)
            .build());
        dayRoutineCount.add(DayRoutineCountResponse.builder()
            .date(LocalDateTime.parse("2025-07-04T00:00:00"))
            .completionRate(50d)
            .build());
        dayRoutineCount.add(DayRoutineCountResponse.builder()
            .date(LocalDateTime.parse("2025-07-05T00:00:00"))
            .completionRate(100d)
            .build());
        dayRoutineCount.add(DayRoutineCountResponse.builder()
            .date(LocalDateTime.parse("2025-07-06T00:00:00"))
            .completionRate(50d)
            .build());

        List<CategoryTotalCountResponse> categoryTotalCount = new ArrayList<>();

        categoryTotalCount.add(CategoryTotalCountResponse.builder()
            .categoryName("요리")
            .totalCount(6L)
            .build());
        categoryTotalCount.add(CategoryTotalCountResponse.builder()
            .categoryName("소비")
            .totalCount(1L)
            .build());
        categoryTotalCount.add(CategoryTotalCountResponse.builder()
            .categoryName("청소/정리")
            .totalCount(1L)
            .build());
        categoryTotalCount.add(CategoryTotalCountResponse.builder()
            .categoryName("건강")
            .totalCount(15L)
            .build());
        categoryTotalCount.add(CategoryTotalCountResponse.builder()
            .categoryName("행정")
            .totalCount(1L)
            .build());
        categoryTotalCount.add(CategoryTotalCountResponse.builder()
            .categoryName("외출")
            .totalCount(1L)
            .build());
        categoryTotalCount.add(CategoryTotalCountResponse.builder()
            .categoryName("세탁/의류")
            .totalCount(1L)
            .build());
        categoryTotalCount.add(CategoryTotalCountResponse.builder()
            .categoryName("쓰레기/환경")
            .totalCount(2L)
            .build());

        List<CategoryRankResponse> categoryRankDTOS = new ArrayList<>();

        categoryRankDTOS.add(CategoryRankResponse.builder()
            .rank(1L)
            .categoryName("건강")
            .completedCount(10L)
            .build());
        categoryRankDTOS.add(CategoryRankResponse.builder()
            .rank(2L)
            .categoryName("요리")
            .completedCount(4L)
            .build());
        categoryRankDTOS.add(CategoryRankResponse.builder()
            .rank(3L)
            .categoryName("쓰레기/환경")
            .completedCount(2L)
            .build());
        categoryRankDTOS.add(CategoryRankResponse.builder()
            .rank(4L)
            .categoryName("청소/정리")
            .completedCount(1L)
            .build());
        categoryRankDTOS.add(CategoryRankResponse.builder()
            .rank(5L)
            .categoryName("행정")
            .completedCount(1L)
            .build());

        Integer totalPoint = 1500;

        String aiComment = "정말 멋진 한 주였어요! 현재 루틴의 70% 이상을 완료하시고, 특히 건강관련 루틴을 꾸준히 실천하신 점이 돋보입니다. 7월 2일과 4일에는 모든 루틴을 완수하는 놀라운 집중력을 보여주었네요!\n\n다음 주에는 하루에 3~4개 정도의 루틴을 꾸준히 완료하는 것을 목표로 삼아보는 건 어떨까요? 매일 비스한 개수의 루틴을 실천하면 리듬을 유지하고 꾸준함을 높이는 데 도움이 될 거에요. 매일매일 조금씩 더 발전하는 당신의 모습을 응원합니다!\n";

        // response에 맞게 매핑
        DashboardWrapper wrapper = DashboardWrapper.builder()
            .routineCount(routineTotalCount)
            .dayRoutineCount(dayRoutineCount)
            .categoryCount(categoryTotalCount)
            .top5(categoryRankDTOS)
            .totalPoint(totalPoint)
            .aiComment(aiComment)
            .build();

        return ResponseEntity.ok(CommonApiResponse.success(wrapper));
    }
}
