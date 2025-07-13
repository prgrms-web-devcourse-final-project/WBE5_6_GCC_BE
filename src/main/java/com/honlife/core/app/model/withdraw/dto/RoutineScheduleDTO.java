package com.honlife.core.app.model.withdraw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoutineScheduleDTO {

    private Long id;

    private LocalDate date;

    @JsonProperty("isDone")
    private Boolean isDone;

    private LocalDateTime createdAt;

    @NotNull
    private Long routine;

}
