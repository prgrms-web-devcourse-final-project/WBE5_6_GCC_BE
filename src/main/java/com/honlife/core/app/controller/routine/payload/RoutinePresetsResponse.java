package com.honlife.core.app.controller.routine.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
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

        @Schema(description = "카테고리 ID", example = "4")
        private Long categoryId;


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

        @Schema(description = "루틴 내용", example = "아침 스트레칭 하기")
        private String content;
    }
}