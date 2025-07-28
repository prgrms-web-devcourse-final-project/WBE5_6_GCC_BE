package com.honlife.core.app.model.admin.routinePreset.service;

import com.honlife.core.app.controller.admin.routine.payload.AdminRoutinePresetSaveRequest;
import com.honlife.core.app.controller.admin.routine.payload.AdminRoutinePresetUpdateRequest;
import com.honlife.core.app.model.admin.routinePreset.dto.RoutinePresetViewDTO;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.routine.domain.RoutinePreset;
import com.honlife.core.app.model.routine.repos.RoutinePresetRepository;
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
  private final CategoryRepository categoryRepository;

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
              .isImportant(routinePreset.getIsImportant())
              .createdAt(routinePreset.getCreatedAt())
              .updatedAt(routinePreset.getUpdatedAt())
              .repeatInterval(routinePreset.getRepeatTerm())
              .startRoutineDate(routinePreset.getStartDate())
              .repeatType(routinePreset.getRepeatType())
              .repeatValue(routinePreset.getRepeatValue())
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
        .isImportant(routinePreset.getIsImportant())
        .createdAt(routinePreset.getCreatedAt())
        .updatedAt(routinePreset.getUpdatedAt())
        .repeatInterval(routinePreset.getRepeatTerm())
        .startRoutineDate(routinePreset.getStartDate())
        .repeatType(routinePreset.getRepeatType())
        .repeatValue(routinePreset.getRepeatValue())
        .emoji(routinePreset.getCategory().getEmoji())
        .build();

    return dto;

  }

  public void createRoutinePreset(AdminRoutinePresetSaveRequest request) {

    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_CATEGORY));

    RoutinePreset routinePreset = RoutinePreset.builder()
        .category(category)
        .triggerTime(request.getTriggerTime())
        .isImportant(request.getIsImportant())
        .content(request.getName())
        .repeatType(request.getRepeatType())
        .repeatValue(request.getRepeatValue())
        .repeatTerm(request.getRepeatInterval())
        .startDate(request.getStartRoutineDate())
        .build();

    routinePresetRepository.save(routinePreset);

  }


  @Transactional
  public void updateRoutinePreset(Long presetId, AdminRoutinePresetUpdateRequest request) {


    RoutinePreset routinePreset = routinePresetRepository.findById(presetId)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_ROUTINE));

    if (request.getName() != null) {
      routinePreset.setContent(request.getName());
    }

    if (request.getTriggerTime() != null) {
      routinePreset.setTriggerTime(request.getTriggerTime());
    }

    if (request.getIsImportant() != null) {
      routinePreset.setIsImportant(request.getIsImportant());
    }

    if (request.getRepeatType() != null) {
      routinePreset.setRepeatType(request.getRepeatType());
    }

    if (request.getRepeatValue() != null) {
      routinePreset.setRepeatValue(request.getRepeatValue());
    }

    if (request.getRepeatInterval() != null) {
      routinePreset.setRepeatTerm(request.getRepeatInterval());
    }

    if (request.getStartRoutineDate() != null) {
      routinePreset.setStartDate(request.getStartRoutineDate());
    }

    if (request.getCategoryId() != null) {
      Category category = categoryRepository.findById(request.getCategoryId())
          .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_CATEGORY));
      routinePreset.setCategory(category);
    }

    routinePreset.setUpdatedAt(LocalDateTime.now());


  }

  @Transactional
  public void deleteRoutinePreset(Long presetId) {
    RoutinePreset routinePreset = routinePresetRepository.findById(presetId)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_ROUTINE));

    routinePreset.setIsActive(false);

  }
}
