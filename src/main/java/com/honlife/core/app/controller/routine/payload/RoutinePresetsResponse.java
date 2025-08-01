package com.honlife.core.app.controller.routine.payload;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class RoutinePresetsResponse {

    private List<PresetItem> presets;

    @Getter
    @Setter
    @Builder
    public static class PresetItem {

        private Long presetId;

        private Long categoryId;

        private String majorCategory;  // 상위 카테고리 이름

        private String name;  // 루틴 이름 (content로부터)

        private String triggerTime;

        private Boolean isImportant;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        private String emoji;

        private String repeatType;

        private Integer repeatTerm;

        private String repeatValue;

        private LocalDate initDate;
    }
}