package com.honlife.core.app.model.dashboard.dto;

import com.honlife.core.app.model.routine.domain.RoutineSchedule;
import com.honlife.core.app.model.routine.dto.RoutineSummaryDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoutineJsonDTO {

    private DashboardWrapperDTO statistics;

    private List<RoutineSummaryDTO> routineSchedules;

}
