package com.honlife.core.app.controller.routine;

import com.honlife.core.app.controller.routine.payload.RoutineDetailResponse;
import com.honlife.core.app.controller.routine.payload.RoutineSaveRequest;
import com.honlife.core.app.controller.routine.payload.RoutineUpdateRequest;
import com.honlife.core.app.controller.routine.payload.RoutinesResponse;
import com.honlife.core.app.controller.routine.payload.RoutinesTodayResponse;
import com.honlife.core.app.model.routine.dto.RoutineDetailDTO;
import com.honlife.core.app.model.routine.dto.RoutineItemDTO;
import com.honlife.core.app.model.routine.dto.RoutineTodayItemDTO;
import com.honlife.core.app.model.routine.service.RoutineService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping(value = "/api/v1/routines", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;


    /**
     * 사용자 루틴 일주일 조회 API
     * @param userDetails 로그인된 사용자 정보
     * @return RoutinesResponse
     */
    @GetMapping("/weekly")
    public ResponseEntity<CommonApiResponse<RoutinesResponse>> getWeeklyUserRoutines(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
            if (date == null) {
                date = LocalDate.now();
            }

            String userEmail = userDetails.getUsername();

            Map<LocalDate, List<RoutineItemDTO>> routines = routineService.getUserWeeklyRoutines(userEmail, date);
            RoutinesResponse response = new RoutinesResponse(routines);

            return ResponseEntity.ok(CommonApiResponse.success(response));

    }


    /**
     * 사용자 루틴 오늘날짜 조회 API
     * @param userDetails 로그인된 사용자 정보
     * @return RoutinesDailyResponse
     */
    @GetMapping("/today")
    public ResponseEntity<CommonApiResponse<RoutinesTodayResponse>> getTodayUserRoutines(
        @AuthenticationPrincipal UserDetails userDetails
    ) {

        String userEmail = userDetails.getUsername();
        List<RoutineTodayItemDTO> routinesResponses = routineService.getTodayRoutines(userEmail);
        RoutinesTodayResponse today = new RoutinesTodayResponse(routinesResponses, LocalDate.now());

        return ResponseEntity.ok(CommonApiResponse.success(today));
    }


    /**
     * 현재 날짜 루틴 단건 조회 API
     * @param routineId 조회할 루틴 ID

     * @return RoutineDetailResponse
     */

    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<RoutineDetailResponse>> getDetailRoutine(
        @PathVariable(name = "id") Long routineId
    ) {

            RoutineDetailDTO responseDto = routineService.getDetailRoutine(routineId);
            RoutineDetailResponse response = RoutineDetailResponse.fromDto(responseDto);

            return ResponseEntity.ok(CommonApiResponse.success(response));
    }



    /**
     * 루틴 등록 API
     * @param routineSaveRequest 등록할 루틴의 정보
     * @param userDetails 로그인된 사용자 정보
     */

    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> createRoutine(
        @RequestBody @Valid final RoutineSaveRequest routineSaveRequest,
        @AuthenticationPrincipal UserDetails userDetails
    ) {


            String userEmail = userDetails.getUsername();
            routineService.createRoutine(routineSaveRequest, userEmail);

            return ResponseEntity
                .status(ResponseCode.CREATED.status())
                .body(CommonApiResponse.noContent());

    }
    /**
     * 루틴 수정 API
     * @param routineId 수정할 루틴 ID
     * @param routineUpdateRequest 수정할 루틴의 정보
     * @return
     */


    @PatchMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> updateRoutine(
        @PathVariable(name = "id") final Long routineId,
        @RequestBody @Valid final RoutineUpdateRequest routineUpdateRequest
    ) {

            routineService.updateRoutine(routineId, routineUpdateRequest);
            return ResponseEntity.ok(CommonApiResponse.noContent());

    }

    /**
     * 루틴 삭제 API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> deleteRoutine(
        @PathVariable(name = "id") final Long routineId
    ) {

            routineService.deleteRoutine(routineId);
            return ResponseEntity.ok(CommonApiResponse.noContent());

    }



}
