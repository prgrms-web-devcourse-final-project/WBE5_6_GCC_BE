package com.honlife.core.app.controller.routine;

import com.honlife.core.app.controller.routine.payload.RoutinePresetsResponse;
import com.honlife.core.app.model.routine.service.RoutinePresetService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/v1/routines/presets", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoutinePresetController {

    private final RoutinePresetService routinePresetService;

    public RoutinePresetController(final RoutinePresetService routinePresetService) {
        this.routinePresetService = routinePresetService;
    }

    /**
     * 추천 루틴 불러오기 API
     * @param categoryId 카테고리 ID
     * @param userDetails 로그인된 사용자 정보
     * @return RoutinePresetsResponse
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<RoutinePresetsResponse>> getRoutinePresets(
        @RequestParam Long categoryId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = userDetails.getUsername();
        if (!userId.equals("user01@test.com")) {
            return ResponseEntity.status(ResponseCode.UNAUTHORIZED.status())
                .body(CommonApiResponse.error(ResponseCode.UNAUTHORIZED));
        }

        // 모킹 데이터 생성
        RoutinePresetsResponse response = new RoutinePresetsResponse();
        List<RoutinePresetsResponse.PresetItem> presets = new ArrayList<>();

        if (categoryId == 1L) {
            // 청소 카테고리 프리셋
            presets.add(RoutinePresetsResponse.PresetItem.builder()
                .presetId(1L)
                .categoryId(1L)
                .content("화장실 청소하기")
                .build());
            presets.add(RoutinePresetsResponse.PresetItem.builder()
                .presetId(4L)
                .categoryId(1L)
                .content("방 정리하기")
                .build());
            presets.add(RoutinePresetsResponse.PresetItem.builder()
                .presetId(6L)
                .categoryId(1L)
                .content("설거지하기")
                .build());
        } else if (categoryId == 2L) {
            // 건강 카테고리 프리셋
            presets.add(RoutinePresetsResponse.PresetItem.builder()
                .presetId(2L)
                .categoryId(2L)
                .content("자기 전 명상 10분")
                .build());
            presets.add(RoutinePresetsResponse.PresetItem.builder()
                .presetId(7L)
                .categoryId(2L)
                .content("물 2L 마시기")
                .build());
        } else if (categoryId == 4L) {
            // 운동 카테고리 프리셋
            presets.add(RoutinePresetsResponse.PresetItem.builder()
                .presetId(3L)
                .categoryId(4L)
                .content("아침 스트레칭 하기")
                .build());
            presets.add(RoutinePresetsResponse.PresetItem.builder()
                .presetId(5L)
                .categoryId(4L)
                .content("플랭크 1분")
                .build());
            presets.add(RoutinePresetsResponse.PresetItem.builder()
                .presetId(8L)
                .categoryId(4L)
                .content("계단 오르기")
                .build());
        }

        response.setPresets(presets);

        // 실제 구현 시에는 다음과 같은 로직 수행:
        // 1. categoryId로 해당 카테고리의 활성화된 프리셋만 조회
        // 2. DTO 변환하여 반환

        return ResponseEntity.ok(CommonApiResponse.success(response));
    }
}