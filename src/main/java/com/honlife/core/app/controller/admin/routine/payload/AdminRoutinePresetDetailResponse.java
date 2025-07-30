package com.honlife.core.app.controller.admin.routine.payload;

import com.honlife.core.app.model.admin.routinePreset.dto.RoutinePresetViewDTO;
import com.honlife.core.app.model.routine.code.RepeatType;
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

    private String name;

    private String triggerTime;

    private Boolean isImportant;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String emoji;

    private RepeatType repeatType;

    private String repeatValue;


    private Integer repeatTerm;


    public static AdminRoutinePresetDetailResponse fromDto(RoutinePresetViewDTO dto) {
        return AdminRoutinePresetDetailResponse.builder()
            .presetId(dto.getPresetId())
            .categoryId(dto.getCategoryId())
            .majorCategory(dto.getMajorCategory())
            .name(dto.getName())
            .triggerTime(dto.getTriggerTime())
            .isImportant(dto.getIsImportant())
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .emoji(dto.getEmoji())
            .repeatType(dto.getRepeatType())
            .repeatValue(dto.getRepeatValue())
            .repeatTerm(dto.getRepeatTerm())
            .build();
    }

}