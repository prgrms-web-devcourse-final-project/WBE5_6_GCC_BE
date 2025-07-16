package com.honlife.core.app.controller.routine.payload;

import com.honlife.core.app.model.routine.dto.RoutineItemDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Schema(description = "사용자 루틴 조회 응답")
public class RoutinesResponse {


    @Schema(description = "루틴 목록")
    private Map<LocalDate, List<RoutineItemDTO>> routines;


}