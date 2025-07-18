package com.honlife.core.app.controller.routine;

import com.honlife.core.app.controller.routine.payload.RoutinePresetsResponse;
import com.honlife.core.app.model.routine.service.RoutinePresetService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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


@Tag(name = "루틴 프리셋", description = "추천 루틴 관련 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/routines/presets", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearerAuth")
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
    @Operation(
        summary = "추천 루틴 불러오기",
        description = "사용자에게 추천할 루틴 프리셋 목록을 조회합니다.<br><br>" +
            "<strong>사용 시나리오:</strong><br>" +
            "• 루틴 생성 시 카테고리 선택 후 해당 카테고리의 추천 루틴 표시<br>" +
            "• 루틴 입력란 밑에 참고할 수 있는 프리셋 예시 제공<br>" +
            "• 사용자가 프리셋을 선택하여 개인 루틴으로 추가 가능<br><br>" +
            "<strong>파라미터:</strong><br>" +
            "• categoryId: 선택한 카테고리의 프리셋만 반환<br><br>" +
            "<strong>응답 데이터:</strong><br>" +
            "• 활성화된 프리셋만 반환<br>" +
            "• 카테고리별 필터링 지원<br>" +
            "• 관련성 높은 추천 제공<br><br>" +
            "*실제 DB에 반영되지 않음*"
    )
    @GetMapping
    public ResponseEntity<CommonApiResponse<RoutinePresetsResponse>> getRoutinePresets(
        @RequestParam
        @Schema(description = "카테고리 ID", example = "1") Long categoryId,
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
