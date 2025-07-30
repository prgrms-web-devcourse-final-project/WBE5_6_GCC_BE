package com.honlife.core.app.controller.routine.payload;

import com.honlife.core.app.model.routine.code.RepeatType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Schema(description = "추천 루틴 불러오기 응답")
public class RoutinePresetsResponse {

    @Schema(description = "프리셋 목록")
    private List<PresetItem> presets;

    @Getter
    @Setter
    @Builder
    @Schema(description = "프리셋 아이템")
    public static class PresetItem {

        @Schema(description = "프리셋 ID", example = "1")
        private Long presetId;

        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "상위 카테고리 이름", example = "청소")
        private String majorCategory;

        @Schema(description = "루틴 이름", example = "화장실 청소하기")
        private String name;

        @Schema(description = "루틴 트리거 시간", example = "23:00")
        private String triggerTime;

        @Schema(description = "중요 루틴 여부", example = "true")
        private Boolean isImportant;

        @Schema(description = "루틴 반복 유형", example = "DAILY")
        private RepeatType repeatType;

        @Schema(description = "루틴 반복 값", example = "월,수,금")
        private String repeatValue;

        @Schema(description = "루틴 반복 주기 (기본 1)", example = "1")
        private Integer repeatTerm;

        @Schema(description = "생성일자", example = "2025-07-20T14:00:00")
        private LocalDateTime createdAt;

        @Schema(description = "수정일자", example = "2025-07-27T08:30:00")
        private LocalDateTime updatedAt;

        @Schema(description = "이모지", example = "🧼")
        private String emoji;

        @Schema(description = "루틴 완료 여부", example = "false")
        private Boolean isDone;

    }
}