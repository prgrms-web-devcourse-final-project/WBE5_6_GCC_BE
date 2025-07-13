package com.honlife.core.app.model.routine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.honlife.core.app.model.routine.code.RepeatType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoutineDTO {

    private Long routineId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    @Size(max = 255)
    private String content;

    @Size(max = 255)
    private String triggerTime;

    @JsonProperty("isImportant")
    private Boolean isImportant;

    private RepeatType repeatType;

    @Size(max = 100)
    private String repeatValue;

    @NotNull
    private Long member;

    @NotNull
    private Long category;

}
