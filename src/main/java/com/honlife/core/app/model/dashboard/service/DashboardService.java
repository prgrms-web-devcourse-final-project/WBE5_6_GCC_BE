package com.honlife.core.app.model.dashboard.service;

import com.honlife.core.app.model.dashboard.dto.CategoryRankDTO;
import com.honlife.core.app.model.dashboard.dto.CategoryCountDTO;
import com.honlife.core.app.model.dashboard.dto.CategoryTotalCountDTO;
import com.honlife.core.app.model.dashboard.dto.DashboardWrapperDTO;
import com.honlife.core.app.model.dashboard.dto.DayRoutineCountDTO;
import com.honlife.core.app.model.dashboard.dto.RoutineTotalCountDTO;
import com.honlife.core.app.model.point.code.PointLogType;
import com.honlife.core.app.model.point.repos.PointLogRepository;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class DashboardService {

    private final RoutineScheduleRepository routineScheduleRepository;
    private final PointLogRepository pointLogRepository;
    private final AICommentService aiService;

    public DashboardWrapperDTO getDashBoardData(String userEmail, LocalDateTime startDateTime) {
        LocalDate startDate = startDateTime.toLocalDate().with(DayOfWeek.MONDAY);
        LocalDate endDate = LocalDate.now();
        if(startDate.plusDays(7).isBefore(LocalDate.now()))
            endDate = startDate.plusDays(7);

        // 총 루틴 개수, 총 완료 루틴 개수 (주)
        RoutineTotalCountDTO routineTotalCountDTO = routineScheduleRepository.countRoutineScheduleByMemberAndDateAndIsDone(userEmail,startDate,endDate);

        // 총 루틴 개수, 총 완료 루틴 개수 (일)
        List<DayRoutineCountDTO> dayRoutineCountDTOs = routineScheduleRepository.countRoutineSchedulesGroupByDate(userEmail,startDate,endDate);

        // 카테고리 이름, 카테고리 참조 루틴 개수
        List<CategoryCountDTO> categoryCountDTOS = routineScheduleRepository.countRoutineSchedulesGroupByCategory(userEmail, startDate, endDate, null);
        Map<String, Long> countMap = countByMajorCategory(categoryCountDTOS);
        List<CategoryTotalCountDTO> categoryTotalCountDTOs = new ArrayList<>();
        for(String categoryName: countMap.keySet()){
            categoryTotalCountDTOs.add(new CategoryTotalCountDTO(categoryName, countMap.get(categoryName)));
        }

        // 카테고리 이름, 카테고리 참조 완료 루틴 개수
        List<CategoryCountDTO> categoryTop5Count = routineScheduleRepository.countRoutineSchedulesGroupByCategory(userEmail, startDate, endDate, true);

        List<CategoryRankDTO> top5CategoryRankDTOS = countByMajorCategory(categoryTop5Count).entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(5)
            .map(entry -> new CategoryRankDTO(entry.getKey(), entry.getValue()))
            .toList();

        long rank =1L;
        for(CategoryRankDTO dto: top5CategoryRankDTOS){
            dto.setRank(rank++);
        }

        // 얻은 누적 포인트량 (주)
        Integer currentPoint = pointLogRepository.getTotalPointByDate(startDate.atStartOfDay(), endDate.atStartOfDay(), PointLogType.GET, userEmail);


        DashboardWrapperDTO dashboardDTO = DashboardWrapperDTO.builder()
            .routineCount(routineTotalCountDTO)
            .dayRoutineCount(dayRoutineCountDTOs)
            .categoryCount(categoryTotalCountDTOs)
            .top5(top5CategoryRankDTOS)
            .totalPoint(currentPoint)
            .build();

        // ai 추가
        String aiComment = aiService.getOrCreateAIComment(userEmail, startDate, dashboardDTO);
        dashboardDTO.setAiComment(aiComment);

        return dashboardDTO;

    }

    private static Map<String, Long> countByMajorCategory(List<CategoryCountDTO> categoryCountDTOS) {
        // 대분류 카테고리를 가져와 이름 별로 카운트
        Map<String, Long> countMap = new HashMap<>();
        for(CategoryCountDTO categoryTotalCountDTO : categoryCountDTOS){
            if(categoryTotalCountDTO.getParentName()!=null)
                countMap.put(categoryTotalCountDTO.getParentName(), countMap.getOrDefault(categoryTotalCountDTO.getParentName(),0L)+ categoryTotalCountDTO.getTotalCount());
            else
                countMap.put(categoryTotalCountDTO.getCategoryName(), countMap.getOrDefault(categoryTotalCountDTO.getCategoryName(),0L)+ categoryTotalCountDTO.getTotalCount());
        }
        return countMap;
    }
}
