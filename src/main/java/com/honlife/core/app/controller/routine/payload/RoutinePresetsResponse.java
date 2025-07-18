package com.honlife.core.app.controller.routine.payload;

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

        private String content;
    }
}