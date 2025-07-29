package com.honlife.core.app.model.routine.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoutineSummaryDTO {

    private LocalDate scheduledDate;

    private String categoryName;

    private String routineContent;

    private Boolean isDone;

}
