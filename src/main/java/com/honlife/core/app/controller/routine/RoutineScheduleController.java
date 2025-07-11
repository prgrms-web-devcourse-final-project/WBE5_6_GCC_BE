package com.honlife.core.app.controller.routine;

import com.honlife.core.app.controller.routine.payload.RoutineScheduleCompleteRequest;
import com.honlife.core.app.model.routine.service.RoutineScheduleService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "루틴 스케줄", description = "루틴 스케줄 관련 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/routines/schedules", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearerAuth")
public class RoutineScheduleController {

    private final RoutineScheduleService routineScheduleService;

    public RoutineScheduleController(final RoutineScheduleService routineScheduleService) {
        this.routineScheduleService = routineScheduleService;
    }

    /**
     * 루틴 스케줄 완료/취소 API
     * @param scheduleId 완료/취소할 스케줄 ID
     * @param request 완료/취소 요청 정보
     * @param userDetails 로그인된 사용자 정보
     * @param bindingResult validation
     * @return
     */
    @Operation(
        summary = "루틴 스케줄 완료/취소",
        description = "특정 날짜의 루틴 스케줄을 완료하거나 취소합니다.<br><br>" +
            "<strong>사용 시나리오:</strong><br>" +
            "• 사용자가 특정 날짜의 루틴 목록을 조회 후, 각 루틴의 scheduleId를 사용<br>" +
            "• 완료 버튼 클릭 시: isDone = true<br>" +
            "• 취소 버튼 클릭 시: isDone = false<br>" +
            "• 미완료 상태로 날짜가 지나가면 프론트에서 실패로 표시<br><br>" +
            "<strong>예시:</strong><br>" +
            "매주 월,수,금 반복하는 '운동하기' 루틴에서<br>" +
            "수요일 스케줄만 완료 처리하고 싶을 때 해당 scheduleId 사용<br><br>" +
            "*실제 DB에 반영되지 않음*"
    )
    @PatchMapping("/{scheduleId}/complete")
    public ResponseEntity<CommonApiResponse<Void>> completeRoutineSchedule(
        @PathVariable(name = "scheduleId")
        @Schema(description = "루틴 스케줄 ID", example = "1") final Long scheduleId,
        @RequestBody @Valid final RoutineScheduleCompleteRequest request,
        @AuthenticationPrincipal UserDetails userDetails,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        String userId = userDetails.getUsername();
        if (!userId.equals("user01@test.com")) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
        }

        // 존재하지 않는 스케줄 ID로 접근 (모킹용)
        if (scheduleId != 1L && scheduleId != 2L) {
            return ResponseEntity
                .status(ResponseCode.NOT_FOUND_ROUTINE.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ROUTINE));
        }

        // 실제 구현 시에는 다음과 같은 로직 수행:
        // 1. scheduleId로 RoutineSchedule 조회
        // 2. 해당 스케줄이 현재 사용자의 것인지 검증
        // 3. isDone 상태 업데이트
        // 4. 완료 시 포인트 지급 로직 (중복 지급 방지)
        // 5. 취소 시 포인트 회수 로직 (필요한 경우)

        return ResponseEntity.ok(CommonApiResponse.noContent());
    }
}