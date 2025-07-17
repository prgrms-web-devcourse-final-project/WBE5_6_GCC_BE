package com.honlife.core.app.controller.routine;

import com.honlife.core.app.controller.routine.payload.RoutinePresetsResponse;
import com.honlife.core.app.controller.routine.payload.RoutinePresetsResponse.PresetItem;
import com.honlife.core.app.model.routine.service.RoutinePresetService;
import com.honlife.core.infra.response.CommonApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/routines/presets", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoutinePresetController {

    private final RoutinePresetService routinePresetService;

    /**
     * 추천 루틴 불러오기 API
     * @param categoryId 카테고리 ID
     * @return RoutinePresetsResponse
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<RoutinePresetsResponse>> getRoutinePresets(
        @RequestParam Long categoryId
    ) {

        List<PresetItem> presetItems= routinePresetService.getRoutinePresets(categoryId);

        RoutinePresetsResponse response = new RoutinePresetsResponse();
        response.setPresets(presetItems);

        return ResponseEntity.ok(CommonApiResponse.success(response));
    }
}