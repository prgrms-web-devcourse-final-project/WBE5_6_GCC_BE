package com.honlife.core.app.controller.routine.payload;

import com.honlife.core.app.model.routine.dto.RoutineDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.honlife.core.app.model.routine.code.RepeatType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutineDetailResponse {

    private Long routineId;

    private Long categoryId;

    private String majorCategory;

    private String subCategory;

    private String name;

    private String triggerTime;

    private Boolean isImportant;

    private RepeatType repeatType;

    private String repeatValue;


    public static RoutineDetailResponse fromDto(RoutineDetailDTO dto) {

        return RoutineDetailResponse.builder()
            .routineId(dto.getRoutineId())
            .categoryId(dto.getCategoryId())
            .majorCategory(dto.getMajorCategory())
            .subCategory(dto.getSubCategory())
            .name(dto.getName())
            .triggerTime(dto.getTriggerTime())
            .isImportant(dto.getIsImportant())
            .repeatType(dto.getRepeatType())
            .repeatValue(dto.getRepeatValue())
            .build();
    }
}