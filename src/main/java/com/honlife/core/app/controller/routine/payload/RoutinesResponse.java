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
@Schema(description = "사용자 루틴 조회 응답")
@AllArgsConstructor
@NoArgsConstructor
public class RoutinesResponse {


    @Schema(description = "루틴 목록")
    private Map<LocalDate, List<RoutineItemDTO>> routines;


}