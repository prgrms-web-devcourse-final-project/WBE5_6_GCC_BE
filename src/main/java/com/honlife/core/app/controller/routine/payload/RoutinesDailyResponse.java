package com.honlife.core.app.controller.routine.payload;

import com.honlife.core.app.model.routine.dto.RoutineItemDTO;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoutinesDailyResponse {

  private List<RoutineItemDTO> routines;

  private LocalDate date;

  public RoutinesDailyResponse(List<RoutineItemDTO> routines, LocalDate date) {
    this.routines = routines;
    this.date = date;
  }
}
