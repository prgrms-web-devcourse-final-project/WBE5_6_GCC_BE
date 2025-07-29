package com.honlife.core.app.controller.admin.routine;

import com.honlife.core.app.controller.admin.routine.payload.AdminRoutinePresetDetailResponse;
import com.honlife.core.app.controller.admin.routine.payload.AdminRoutinePresetSaveRequest;
import com.honlife.core.app.controller.admin.routine.payload.AdminRoutinePresetUpdateRequest;
import com.honlife.core.app.controller.admin.routine.payload.AdminRoutinePresetsResponse;
import com.honlife.core.app.model.admin.routinePreset.dto.RoutinePresetViewDTO;
import com.honlife.core.app.model.admin.routinePreset.service.AdminRoutinePresetService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/admin/routines/presets", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminRoutinePresetController {

  private final AdminRoutinePresetService adminRoutinePresetService;


  /**
   * 추천 루틴 프리셋 목록 조회 API
   * @return AdminRoutinePresetsResponse
   */
  @GetMapping
  public ResponseEntity<CommonApiResponse<AdminRoutinePresetsResponse>> getAllRoutinePresets(
      @RequestParam(value = "id", required = false) Long categoryId
  ) {

    List<RoutinePresetViewDTO> routineDetailDTO = adminRoutinePresetService.getAllRoutinePresets(categoryId);

    AdminRoutinePresetsResponse response = new AdminRoutinePresetsResponse();
    response.setPresets(routineDetailDTO);

    return ResponseEntity.ok(CommonApiResponse.success(response));

  }

  /**
   * 특정 추천 루틴 프리셋 조회 API
   * @param presetId 프리셋 ID
   * @return AdminRoutinePresetDetailResponse
   */
  @GetMapping("/{id}")
  public ResponseEntity<CommonApiResponse<AdminRoutinePresetDetailResponse>> getRoutinePreset(
      @PathVariable(name = "id") final Long presetId
  ) {

    RoutinePresetViewDTO routineDetailDTO = adminRoutinePresetService.getRoutinePreset(presetId);

    AdminRoutinePresetDetailResponse response = AdminRoutinePresetDetailResponse.fromDto(routineDetailDTO);

    return ResponseEntity.ok(CommonApiResponse.success(response));
  }


  /**
   * 추천 루틴 프리셋 생성 API
   * @param request 프리셋 생성 요청 정보
   * @return
   */

  @PostMapping
  public ResponseEntity<CommonApiResponse<Void>> createRoutinePreset(
      @RequestBody @Valid final AdminRoutinePresetSaveRequest request
  ) {

    adminRoutinePresetService.createRoutinePreset(request);

    return ResponseEntity.status(ResponseCode.CREATED.status())
        .body(CommonApiResponse.noContent());
  }

  /**
   * 추천 루틴 프리셋 수정 API
   * @param presetId 수정할 프리셋 ID
   * @param request 프리셋 수정 요청 정보
   * @return
   */
  @PatchMapping("/{id}")
  public ResponseEntity<CommonApiResponse<Void>> updateRoutinePreset(
      @PathVariable(name = "id") final Long presetId,
      @RequestBody @Valid final AdminRoutinePresetUpdateRequest request
  ) {

    adminRoutinePresetService.updateRoutinePreset(presetId, request);

    return ResponseEntity.ok(CommonApiResponse.noContent());
  }

  /**
   * 추천 루틴 프리셋 삭제 API
   * @param presetId 삭제할 프리셋 ID
   * @return
   */

  @DeleteMapping("/{id}")
  public ResponseEntity<CommonApiResponse<Void>> deleteRoutinePreset(
      @PathVariable(name = "id") final Long presetId
  ) {

    adminRoutinePresetService.deleteRoutinePreset(presetId);

    return ResponseEntity.ok(CommonApiResponse.noContent());
  }
}
