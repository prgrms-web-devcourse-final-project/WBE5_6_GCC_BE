package com.honlife.core.app.controller.routine.payload;

import com.honlife.core.app.model.routine.code.RepeatType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoutineSaveRequest {


    @NotBlank(message = "루틴 이름은 필수입니다")
    @Size(max = 255, message = "루틴 내용은 255자를 초과할 수 없습니다")
    private String name;

    private Long categoryid;


    @NotNull(message = "루틴 시작 날짜는 필수입니다")
    private LocalDate startRoutineDate;

    @Size(max = 255, message = "트리거 시간은 255자를 초과할 수 없습니다")
    private String triggerTime;

    private Boolean isImportant = false;

    private RepeatType repeatType = RepeatType.DAILY;

    @Size(max = 100, message = "반복 값은 100자를 초과할 수 없습니다")
    private String repeatValue;

    private Integer repeatTerm = 1;
}