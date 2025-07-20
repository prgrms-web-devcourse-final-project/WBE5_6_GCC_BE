package com.honlife.core.app.controller.admin.routine.payload;

import com.honlife.core.app.model.admin.routinePreset.dto.RoutinePresetViewDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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

    private String categoryName;

    private String content;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public static AdminRoutinePresetDetailResponse from(RoutinePresetViewDTO dto) {
        return AdminRoutinePresetDetailResponse.builder()
            .presetId(dto.getPresetId())
            .categoryId(dto.getCategoryId())
            .categoryName(dto.getCategoryName())
            .content(dto.getContent())
            .isActive(dto.getIsActive())
            .createdAt(dto.getCreatedAt())
            .updatedAt(dto.getUpdatedAt())
            .build();
    }

}