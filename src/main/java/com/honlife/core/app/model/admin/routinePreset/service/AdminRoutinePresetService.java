package com.honlife.core.app.model.admin.routinePreset.service;

import com.honlife.core.app.controller.admin.routine.payload.AdminRoutinePresetDetailResponse;
import com.honlife.core.app.model.admin.routinePreset.dto.RoutinePresetViewDTO;
import com.honlife.core.app.model.routine.domain.RoutinePreset;
import com.honlife.core.app.model.routine.repos.RoutinePresetRepository;
import com.honlife.core.app.model.routine.repos.RoutineRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminRoutinePresetService {

  private final RoutinePresetRepository routinePresetRepository;

  /**
   * 추천 루틴 프리셋 목록 조회 service
   * @return List<RoutinePresetViewDTO>
   */
  public List<RoutinePresetViewDTO> getAllRoutinePresets(Long categoryId) {
    // categoryId가 있으면 해당 카테고리의 프리셋만 조회
    // categoryId가 없으면 모든 프리셋 조회 (활성/비활성 포함)

    List<RoutinePreset> routinePresetList;
    if(categoryId == null){
      routinePresetList = routinePresetRepository.findAllWithCategory();
    }else{
      routinePresetList = routinePresetRepository.getRoutinePresetWithCategoryId(categoryId);
    }

    //만약에 category가 null이면 예외 던짐
    if(routinePresetList.isEmpty()){
      throw new CommonException(ResponseCode.NOT_FOUND_CATEGORY);
    }

    //DTO 변환하여 반환
    return routinePresetList.stream()
        .sorted(Comparator.comparing(RoutinePreset::getCreatedAt))
        .map(routinePreset ->
            RoutinePresetViewDTO.builder()
                .presetId(routinePreset.getId())
                .categoryId(routinePreset.getCategory().getId())
                .categoryName(routinePreset.getCategory().getName())
                .content(routinePreset.getContent())
                .isActive(routinePreset.getIsActive())
                .createdAt(routinePreset.getCreatedAt())
                .updatedAt(routinePreset.getUpdatedAt())
                .build()
        )
        .collect(Collectors.toList());
  }

  /**
   * 추천 루틴 프리셋 단건 조회 조회 service
   * @return RoutinePresetViewDTO
   */
  public RoutinePresetViewDTO getRoutinePreset(Long presetId) {

    RoutinePreset routinePreset = routinePresetRepository.findWithCategoryById(presetId)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

    //DTO 변환하여 반환
    RoutinePresetViewDTO dto = RoutinePresetViewDTO.builder()
        .presetId(routinePreset.getId())
        .categoryId(routinePreset.getCategory().getId())
        .categoryName(routinePreset.getCategory().getName())
        .content(routinePreset.getContent())
        .isActive(routinePreset.getIsActive())
        .createdAt(routinePreset.getCreatedAt())
        .updatedAt(routinePreset.getUpdatedAt())
        .build();

    return dto;

  }
}
