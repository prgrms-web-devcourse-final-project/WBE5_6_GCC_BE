package com.honlife.core.app.model.admin.routinePreset.service;

import com.honlife.core.app.model.admin.routinePreset.dto.RoutinePresetViewDTO;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.routine.domain.RoutinePreset;
import com.honlife.core.app.model.routine.repos.RoutinePresetRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
      routinePresetList = routinePresetRepository.findAllActiveWithCategory();
    }else{
      routinePresetList = routinePresetRepository.getActiveRoutinePresetWithCategoryId(categoryId);
    }

    //만약에 category가 null이면 예외 던짐
    if(routinePresetList.isEmpty()){
      throw new CommonException(ResponseCode.NOT_FOUND_CATEGORY);
    }

    //DTO 변환하여 반환
    return routinePresetList.stream()
        .sorted(Comparator.comparing(RoutinePreset::getCreatedAt))
        .map(routinePreset -> {
          Category category = routinePreset.getCategory();
          CategoryType type = category.getType();

          Category parentCategory = null;
          Category childCategory = null;

          if (type == CategoryType.DEFAULT || type == CategoryType.MAJOR) {
            parentCategory = category; // 이미 연관된 엔티티
            childCategory = null;
          } else {
            childCategory = category;
            parentCategory = category.getParent();// 연관관계로 접근 가능해야 함
          }
          return RoutinePresetViewDTO.builder()
              .presetId(routinePreset.getId())
              .categoryId(routinePreset.getCategory().getId())
              .majorCategory(parentCategory != null ? parentCategory.getName() : null)
              .subCategory(childCategory != null ? childCategory.getName() : null)
              .name(routinePreset.getContent())
              .triggerTime(routinePreset.getTriggerTime())
              .isImportant(routinePreset.isImportant())
              .createdAt(routinePreset.getCreatedAt())
              .updatedAt(routinePreset.getUpdatedAt())
              .repeatType(routinePreset.getRepeatType())
              .repeatValue(routinePreset.getRepeatValue())
              .initDate(routinePreset.getInitDate())
              .build();
        })
        .collect(Collectors.toList());


  }

  /**
   * 추천 루틴 프리셋 단건 조회 조회 service
   * @return RoutinePresetViewDTO
   */
  public RoutinePresetViewDTO getRoutinePreset(Long presetId) {

    RoutinePreset routinePreset = routinePresetRepository.findWithCategoryById(presetId)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
    Category category = routinePreset.getCategory();
    CategoryType type = category.getType();

    Category parentCategory = null;
    Category childCategory = null;

    if (type == CategoryType.DEFAULT || type == CategoryType.MAJOR) {
      parentCategory = category; // 이미 연관된 엔티티
      childCategory = null;
    } else {
      childCategory = category;
      parentCategory = category.getParent();// 연관관계로 접근 가능해야 함
    }

    //DTO 변환하여 반환
    RoutinePresetViewDTO dto = RoutinePresetViewDTO.builder()
        .presetId(routinePreset.getId())
        .categoryId(routinePreset.getCategory().getId())
        .majorCategory(parentCategory != null ? parentCategory.getName() : null)
        .subCategory(childCategory != null ? childCategory.getName() : null)
        .name(routinePreset.getContent())
        .triggerTime(routinePreset.getTriggerTime())
        .isImportant(routinePreset.isImportant())
        .createdAt(routinePreset.getCreatedAt())
        .updatedAt(routinePreset.getUpdatedAt())
        .emoji(routinePreset.getCategory().getEmoji())
        .repeatType(routinePreset.getRepeatType())
        .repeatValue(routinePreset.getRepeatValue())
        .initDate(routinePreset.getInitDate())
        .build();

    return dto;

  }
}
