package com.honlife.core.app.controller.routine;

import com.honlife.core.app.controller.routine.payload.RoutineDetailResponse;
import com.honlife.core.app.controller.routine.payload.RoutineSaveRequest;
import com.honlife.core.app.controller.routine.payload.RoutinesResponse;
import com.honlife.core.app.model.routine.code.RepeatType;
import com.honlife.core.app.model.routine.service.RoutineService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "루틴", description = "루틴 관련 api 입니다.")
@RestController
@RequestMapping(value = "/api/v1/routines", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearerAuth")
public class RoutineController {

    private final RoutineService routineService;

    public RoutineController(final RoutineService routineService) {
        this.routineService = routineService;
    }

    /**
     * 사용자 루틴 조회 API
     * @param date 조회할 날짜 (없으면 오늘 날짜)
     * @param userDetails 로그인된 사용자 정보
     * @return UserRoutinesPayload
     */
    @Operation(summary = "사용자 루틴 조회", description = "특정 날짜의 사용자 루틴 목록을 조회합니다. <br>date를 넣지 않으면 오늘 날짜 기준으로 조회됩니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<RoutinesResponse>> getUserRoutines(
        @Schema(name = "date", description = "조회할 날짜를 적어주세요", example = "2025-01-15")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = userDetails.getUsername();
        if (date == null) {
            date = LocalDate.now();
        }

        if (userId.equals("user01@test.com")) {
            RoutinesResponse response = new RoutinesResponse();
            response.setDate(date);

            List<RoutinesResponse.RoutineItem> routines = new ArrayList<>();
            routines.add(RoutinesResponse.RoutineItem.builder()
                .scheduleId(1L)
                .routineId(1L)
                .majorCategory("청소")
                .subCategory("화장실 청소")
                .name("변기 청소하기")
                .triggerTime("09:00")
                .isDone(true)
                .isImportant(false)
                .build());
            routines.add(RoutinesResponse.RoutineItem.builder()
                .scheduleId(2L)
                .routineId(2L)
                .majorCategory("건강")
                .subCategory(null)
                .name("아침 운동하기")
                .triggerTime("07:00")
                .isDone(false)
                .isImportant(true)
                .build());

            response.setRoutines(routines);
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }

        return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
            .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
    }

    /**
     * 특정 루틴 조회 API
     * @param routineId 조회할 루틴 ID
     * @param userDetails 로그인된 사용자 정보
     * @return RoutinePayload
     */
    @Operation(summary = "특정 루틴 조회", description = "특정 루틴의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<RoutineDetailResponse>> getRoutine(
        @PathVariable(name = "id")
        @Schema(description = "루틴 ID", example = "1") final Long routineId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = userDetails.getUsername();
        if (userId.equals("user01@test.com") && routineId == 1L) {
            RoutineDetailResponse response = RoutineDetailResponse.builder()
                .routineId(1L)
                .categoryId(1L)
                .majorCategory("청소")
                .subCategory("화장실 청소")
                .name("변기 청소하기")
                .triggerTime("09:00")
                .isImportant(false)
                .repeatType(RepeatType.WEEKLY)
                .repeatValue("1,3,5")
                .build();

            return ResponseEntity.ok(CommonApiResponse.success(response));
        }

        // 존재하지 않는 루틴
        return ResponseEntity.status(ResponseCode.NOT_FOUND_ROUTINE.status())
            .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ROUTINE));
    }

    /**
     * 루틴 등록 API
     * @param routineSaveRequest 등록할 루틴의 정보
     * @param userDetails 로그인된 사용자 정보
     * @param bindingResult validation
     * @return
     */
    @Operation(summary = "루틴 등록", description = "새로운 루틴을 등록합니다. <br>카테고리 ID와 루틴 내용은 필수입니다. <br><br>" +
        "<strong>RepeatType 설명:</strong><br>" +
        "• DAILY: 매일 반복 (repeatValue 불필요)<br>" +
        "• WEEKLY: 매주 특정 요일 반복 (repeatValue 예시: '1,3,5' = 월,수,금)<br>" +
        "• MONTHLY: 매월 특정 일 반복 (repeatValue 예시: '1,15,30' = 매월 1일,15일,30일)<br>" +
        "• CUSTOM: 사용자 정의 반복 패턴<br>" +
        "요일 번호: 1=월요일~7=일요일<br><br>*실제 DB에 반영되지 않음*")
    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> createRoutine(
        @RequestBody @Valid final RoutineSaveRequest routineSaveRequest,
        @AuthenticationPrincipal UserDetails userDetails,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        String userId = userDetails.getUsername();
        if (userId.equals("user01@test.com")) {
            // 실제 구현 시에는 routineSaveRequest를 RoutineDTO로 변환하여 routineService.create() 호출
            // RepeatType이 NONE인 경우 RoutineSchedule도 함께 생성
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonApiResponse.noContent());
        }

        return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
            .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
    }

    /**
     * 루틴 수정 API
     * @param routineId 수정할 루틴 ID
     * @param routineSaveRequest 수정할 루틴의 정보
     * @param userDetails 로그인된 사용자 정보
     * @param bindingResult validation
     * @return
     */
    @Operation(summary = "루틴 수정", description = "특정 루틴을 수정합니다. <br>id가 1, 2인 데이터에 대해서만 수정 요청을 할 수 있도록 하였습니다. <br><br>" +
        "<strong>RepeatType 설명:</strong><br>" +
        "• DAILY: 매일 반복 (repeatValue 불필요)<br>" +
        "• WEEKLY: 매주 특정 요일 반복 (repeatValue 예시: '1,3,5' = 월,수,금)<br>" +
        "• MONTHLY: 매월 특정 일 반복 (repeatValue 예시: '1,15,30' = 매월 1일,15일,30일)<br>" +
        "• CUSTOM: 사용자 정의 반복 패턴<br>" +
        "요일 번호: 1=월요일~7=일요일<br><br>*실제 DB에 반영되지 않음*")
    @PatchMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> updateRoutine(
        @PathVariable(name = "id")
        @Schema(description = "루틴 ID", example = "1") final Long routineId,
        @RequestBody @Valid final RoutineSaveRequest routineSaveRequest,
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

        // 존재하지 않는 루틴 아이디로 접근
        if (routineId != 1L && routineId != 2L) {
            return ResponseEntity
                .status(ResponseCode.NOT_FOUND_ROUTINE.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ROUTINE));
        }

        // 실제 구현 시에는 기존 루틴 타입과 새로운 타입을 비교하여
        // 스케줄 재생성 또는 기존 스케줄 업데이트 처리
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 루틴 삭제 API
     * @param routineId 삭제할 루틴 ID
     * @param userDetails 로그인된 사용자 정보
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "루틴 삭제", description = "특정 루틴을 삭제합니다. <br>id가 1, 2인 데이터에 대해서만 삭제 요청을 할 수 있도록 하였습니다. <br>*실제 DB에 반영되지 않음*")
    public ResponseEntity<CommonApiResponse<Void>> deleteRoutine(
        @PathVariable(name = "id")
        @Schema(description = "루틴 ID", example = "1") final Long routineId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = userDetails.getUsername();
        if (!userId.equals("user01@test.com")) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_MEMBER.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_MEMBER));
        }

        // 존재하지 않는 루틴 아이디로 접근
        if (routineId != 1L && routineId != 2L) {
            return ResponseEntity
                .status(ResponseCode.NOT_FOUND_ROUTINE.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ROUTINE));
        }

        // 실제 구현 시에는 루틴과 관련된 모든 스케줄도 함께 삭제 처리
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }
}
