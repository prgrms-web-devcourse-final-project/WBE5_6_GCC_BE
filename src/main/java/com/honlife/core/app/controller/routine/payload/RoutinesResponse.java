package com.honlife.core.app.controller.routine.payload;

import com.honlife.core.app.model.routine.dto.RoutineItemDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoutinesResponse {


    private Map<LocalDate, List<RoutineItemDTO>> routines;


}