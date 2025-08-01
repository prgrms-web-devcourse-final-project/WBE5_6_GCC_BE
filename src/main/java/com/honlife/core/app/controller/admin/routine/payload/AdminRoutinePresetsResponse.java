package com.honlife.core.app.controller.admin.routine.payload;

import com.honlife.core.app.model.admin.routinePreset.dto.RoutinePresetViewDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRoutinePresetsResponse {

  private List<RoutinePresetViewDTO> presets;


}