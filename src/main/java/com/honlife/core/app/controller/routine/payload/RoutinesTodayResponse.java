package com.honlife.core.app.controller.routine.payload;

import com.honlife.core.app.model.routine.dto.RoutineItemDTO;
import com.honlife.core.app.model.routine.dto.RoutineTodayItemDTO;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoutinesTodayResponse {

  private List<RoutineTodayItemDTO> routines;

  private LocalDate date;

  public RoutinesTodayResponse(List<RoutineTodayItemDTO> routines, LocalDate date) {
    this.routines = routines;
    this.date = date;
  }
}