package com.honlife.core.app.controller.admin.routine.payload;

import com.honlife.core.app.model.admin.routinePreset.dto.RoutinePresetViewDTO;
import com.honlife.core.app.model.routine.code.RepeatType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoutinePresetDetailResponse {

    private Long presetId;

    private Long categoryId;

    private String majorCategory;

    private String subCategory;

    private String name;

    private String triggerTime;

    private Boolean isImportant;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int repeatInterval;

    private LocalDate startRoutineDate;

    private RepeatType repeatType;

    private String repeatValue;


    public static AdminRoutinePresetDetailResponse fromDto(RoutinePresetViewDTO dto) {
        return AdminRoutinePresetDetailResponse.builder()
            .presetId(dto.getPresetId())
            .categoryId(dto.getCategoryId())
            .majorCategory(dto.getMajorCategory())
            .subCategory(dto.getSubCategory())
            .name(dto.getName())
            .triggerTime(dto.getTriggerTime())
            .isImportant(dto.getIsImportant())
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .repeatInterval(dto.getRepeatInterval())
            .startRoutineDate(dto.getStartRoutineDate())
            .repeatType(dto.getRepeatType())
            .repeatValue(dto.getRepeatValue())
            .build();
    }

}