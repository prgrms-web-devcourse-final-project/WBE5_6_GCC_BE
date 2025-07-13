package com.honlife.core.app.controller.routine.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "루틴 스케줄 완료/취소 요청")
public class RoutineScheduleCompleteRequest {

    @NotNull(message = "완료 상태는 필수입니다")
    @Schema(description = "완료 여부", example = "true", required = true)
    private Boolean isDone;
}