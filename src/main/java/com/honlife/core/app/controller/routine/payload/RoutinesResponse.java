package com.honlife.core.app.controller.routine.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Schema(description = "사용자 루틴 조회 응답")
public class RoutinesResponse {

    @Schema(description = "조회 날짜", example = "2025-01-15")
    private LocalDate date;
    @Schema(description = "루틴 목록")
    private Map<String, List<RoutineItem>> routines;

    @Getter
    @Setter
    @Builder
    @Schema(description = "루틴 아이템")
    public static class RoutineItem {

        @Schema(description = "스케줄 ID", example = "1")
        private Long scheduleId;

        @Schema(description = "루틴 ID", example = "1")
        private Long routineId;

        @Schema(description = "대분류 카테고리", example = "청소")
        private String majorCategory;

        @Schema(description = "소분류 카테고리", example = "화장실 청소")
        private String subCategory;

        @Schema(description = "루틴 이름", example = "변기 청소하기")
        private String name;

        @Schema(description = "트리거 시간", example = "09:00")
        private String triggerTime;

        @Schema(description = "완료 여부", example = "true")
        private Boolean isDone;

        @Schema(description = "중요 루틴 여부", example = "false")
        private Boolean isImportant;

        @Schema(description = "해당 날짜", example = "2025-11-12")
        private LocalDate date;
    }
}