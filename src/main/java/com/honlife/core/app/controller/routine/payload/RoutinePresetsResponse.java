package com.honlife.core.app.controller.routine.payload;

import io.swagger.v3.oas.annotations.media.Schema;
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

        @Schema(description = "루틴 내용", example = "아침 스트레칭 하기")
        private String content;
    }
}