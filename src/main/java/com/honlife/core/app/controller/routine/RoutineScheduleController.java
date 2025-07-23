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
import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/v1/routines/schedules", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RoutineScheduleController {

    private final RoutineScheduleService routineScheduleService;


    /**
     * 루틴 스케줄 완료/취소 API
     * @param scheduleId 완료/취소할 스케줄 ID
     * @param request 완료/취소 요청 정보
     * @param userDetails 로그인된 사용자 정보
     * @param bindingResult validation
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> completeRoutineSchedule(
        @PathVariable(name = "id") final Long scheduleId,
        @RequestBody @Valid final RoutineScheduleCompleteRequest request,
        @AuthenticationPrincipal UserDetails userDetails,
        BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

            String userEmail = userDetails.getUsername();

            routineScheduleService.completeRoutineSchedule(scheduleId, request, userEmail);


        return ResponseEntity.ok(CommonApiResponse.noContent());
    }


}