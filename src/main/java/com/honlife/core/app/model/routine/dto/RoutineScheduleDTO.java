package com.honlife.core.app.model.routine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoutineScheduleDTO {

    private Long id;

    private LocalDateTime date;

    @JsonProperty("isDone")
    private Boolean isDone;

    private LocalDateTime createdAt;

    @NotNull
    private Long routine;

}
