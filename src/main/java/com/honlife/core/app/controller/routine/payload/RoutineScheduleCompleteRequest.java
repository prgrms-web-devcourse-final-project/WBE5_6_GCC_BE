package com.honlife.core.app.controller.routine.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoutineScheduleCompleteRequest {

    @NotNull(message = "완료 상태는 필수입니다")
    private Boolean isDone;
}