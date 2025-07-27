package com.honlife.core.app.model.dashboard.service;

import com.honlife.core.app.model.dashboard.dto.CategoryRankDTO;
import com.honlife.core.app.model.dashboard.dto.CategoryCountDTO;
import com.honlife.core.app.model.dashboard.dto.CategoryTotalCountDTO;
import com.honlife.core.app.model.dashboard.dto.DayRoutineCountDTO;
import com.honlife.core.app.model.dashboard.dto.RoutineTotalCountDTO;
import com.honlife.core.app.model.point.code.PointLogType;
import com.honlife.core.app.model.point.repos.PointLogRepository;
import com.honlife.core.app.model.routine.repos.RoutineScheduleRepository;
import java.time.LocalDate;
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

    /**
     * 루틴에 저장된 카테고리 별로 집계된 것을 대분류 카테고리로 바꾸어 count
     * @param categoryCountDTOS 카테고리 별로 카운트 된 데이터
     * @return Map<String, Long>
     */
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

    /**
     * 주간 루틴 완료 현황을 조회합니다.
     * @param userEmail 조회할 회원
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @return RoutineTotalCountDTO
     */
    public RoutineTotalCountDTO getRoutineTotalCount(String userEmail, LocalDate startDate, LocalDate endDate) {
        // 총 루틴 개수, 총 완료 루틴 개수 (주)
        return routineScheduleRepository.countRoutineScheduleByMemberAndDateBetweenAndIsDone(userEmail,startDate,endDate);
    }

    /**
     * 일간 루틴 완료 현황을 조회합니다.
     * @param userEmail 조회할 회원
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @return List<DayRoutineCountDTO>
     */
    public List<DayRoutineCountDTO> getDayRoutineCounts(String userEmail, LocalDate startDate, LocalDate endDate) {
        // 총 루틴 개수, 총 완료 루틴 개수 (일)
        return routineScheduleRepository.countRoutineSchedulesGroupByDateBetween(userEmail,startDate,endDate);

    }

    /**
     * 주간 카테고리 점유율을 확인하기 위한 카테고리 사용 횟수(해당 카테고리를 참조한 루틴 수)를 조회합니다.
     * @param userEmail 조회할 회원
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @return List<CategoryTotalCountDTO>
     */
    public List<CategoryTotalCountDTO> getCategoryTotalCounts(String userEmail, LocalDate startDate, LocalDate endDate) {

        // 카테고리 이름, 카테고리 참조 루틴 개수
        List<CategoryCountDTO> categoryCountDTOS = routineScheduleRepository.countRoutineSchedulesGroupByCategory(userEmail, startDate, endDate, null);
        
        // 소분류 카테고리로 되어있으면 대분류 카테고리로 집계되도록
        Map<String, Long> countMap = countByMajorCategory(categoryCountDTOS);
        
        // 반환값 만들기
        List<CategoryTotalCountDTO> categoryTotalCountDTOs = new ArrayList<>();
        for(String categoryName: countMap.keySet()){
            categoryTotalCountDTOs.add(new CategoryTotalCountDTO(categoryName, countMap.get(categoryName)));
        }

        return categoryTotalCountDTOs;
    }

    /**
     * 가장 많이 수행한 카테고리를 조회합니다.
     * @param userEmail 조회할 회원
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @return
     */
    public List<CategoryRankDTO> getCategoryRanks(String userEmail, LocalDate startDate, LocalDate endDate) {
        // 카테고리 이름, 카테고리 참조 완료 루틴 개수
        List<CategoryCountDTO> categoryTop5Count = routineScheduleRepository.countRoutineSchedulesGroupByCategory(userEmail, startDate, endDate, true);

        // 가장 많이 수행한 루틴의 카테고리 top5
        List<CategoryRankDTO> top5CategoryRankDTOS = countByMajorCategory(categoryTop5Count).entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(5)
            .map(entry -> new CategoryRankDTO(entry.getKey(), entry.getValue()))
            .toList();

        long rank =1L;
        for(CategoryRankDTO dto: top5CategoryRankDTOS){
            dto.setRank(rank++);
        }

        return top5CategoryRankDTOS;
    }

    public Integer getTotalPoint(String userEmail, LocalDate startDate, LocalDate endDate) {
        // 얻은 누적 포인트량 (주)
        return pointLogRepository.getTotalPointByDateBetween(startDate.atStartOfDay(), endDate.atStartOfDay(), PointLogType.GET, userEmail);
    }
}
