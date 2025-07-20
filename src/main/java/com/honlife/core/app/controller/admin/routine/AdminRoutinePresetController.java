package com.honlife.core.app.controller.admin.routine;

import com.honlife.core.app.controller.admin.routine.payload.AdminRoutinePresetDetailResponse;
import com.honlife.core.app.controller.admin.routine.payload.AdminRoutinePresetSaveRequest;
import com.honlife.core.app.controller.admin.routine.payload.AdminRoutinePresetsResponse;
import com.honlife.core.app.model.admin.routinePreset.dto.RoutinePresetViewDTO;
import com.honlife.core.app.model.admin.routinePreset.service.AdminRoutinePresetService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
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

    AdminRoutinePresetDetailResponse response = AdminRoutinePresetDetailResponse.from(routineDetailDTO);

    return ResponseEntity.ok(CommonApiResponse.success(response));
  }


  /**
   * 추천 루틴 프리셋 생성 API
   * @param request 프리셋 생성 요청 정보
   * @param bindingResult validation
   * @return
   */
  @Operation(
      summary = "추천 루틴 프리셋 생성",
      description = "새로운 추천 루틴 프리셋을 생성합니다.<br><br>" +
          "<strong>권한:</strong> 관리자만 접근 가능<br><br>" +
          "<strong>필수 필드:</strong><br>" +
          "• categoryId: 카테고리 ID<br>" +
          "• content: 루틴 내용 (최대 50자)<br><br>" +
          "<strong>선택 필드:</strong><br>" +
          "• isActive: 활성화 여부 (기본값: true)<br><br>" +
          "*실제 DB에 반영되지 않음*"
  )
  @PostMapping
  public ResponseEntity<CommonApiResponse<Void>> createRoutinePreset(
      @RequestBody @Valid final AdminRoutinePresetSaveRequest request,
      BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity
          .status(ResponseCode.BAD_REQUEST.status())
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }

    // 실제 구현 시에는 다음과 같은 로직 수행:
    // 1. categoryId 존재 여부 검증
    // 2. RoutinePresetDTO 변환
    // 3. routinePresetService.create() 호출
    // 4. 생성된 ID 반환

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(CommonApiResponse.noContent());
  }

  /**
   * 추천 루틴 프리셋 수정 API
   * @param presetId 수정할 프리셋 ID
   * @param request 프리셋 수정 요청 정보
   * @param bindingResult validation
   * @return
   */
  @Operation(
      summary = "추천 루틴 프리셋 수정",
      description = "특정 추천 루틴 프리셋을 수정합니다.<br><br>" +
          "<strong>권한:</strong> 관리자만 접근 가능<br><br>" +
          "<strong>수정 가능 필드:</strong><br>" +
          "• categoryId: 카테고리 변경<br>" +
          "• content: 루틴 내용 변경<br>" +
          "• isActive: 활성화 상태 변경<br><br>" +
          "*실제 DB에 반영되지 않음*"
  )
  @PatchMapping("/{id}")
  public ResponseEntity<CommonApiResponse<Void>> updateRoutinePreset(
      @PathVariable(name = "id")
      @Schema(description = "프리셋 ID", example = "1") final Long presetId,
      @RequestBody @Valid final AdminRoutinePresetSaveRequest request,
      BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity
          .status(ResponseCode.BAD_REQUEST.status())
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }

    // 존재하지 않는 프리셋 ID로 접근
    if (presetId != 1L && presetId != 2L && presetId != 3L) {
      return ResponseEntity.status(ResponseCode.NOT_FOUND.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_FOUND));
    }

    // 실제 구현 시에는 다음과 같은 로직 수행:
    // 1. presetId 존재 여부 검증
    // 2. categoryId 존재 여부 검증
    // 3. RoutinePresetDTO 변환
    // 4. routinePresetService.update() 호출

    return ResponseEntity.ok(CommonApiResponse.noContent());
  }

  /**
   * 추천 루틴 프리셋 삭제 API
   * @param presetId 삭제할 프리셋 ID
   * @return
   */
  @Operation(
      summary = "추천 루틴 프리셋 삭제",
      description = "특정 추천 루틴 프리셋을 삭제합니다.<br><br>" +
          "<strong>권한:</strong> 관리자만 접근 가능<br><br>" +
          "<strong>주의사항:</strong><br>" +
          "• 삭제된 프리셋은 복구할 수 없습니다<br>" +
          "• 사용자가 이미 생성한 루틴에는 영향을 주지 않습니다<br><br>" +
          "*실제 DB에 반영되지 않음*"
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<CommonApiResponse<Void>> deleteRoutinePreset(
      @PathVariable(name = "id")
      @Schema(description = "프리셋 ID", example = "1") final Long presetId
  ) {
    // 존재하지 않는 프리셋 ID로 접근
    if (presetId != 1L && presetId != 2L && presetId != 3L) {
      return ResponseEntity.status(ResponseCode.NOT_FOUND.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_FOUND));
    }

    // 실제 구현 시에는 다음과 같은 로직 수행:
    // 1. presetId 존재 여부 검증
    // 2. routinePresetService.delete() 호출
    // 3. soft delete 권장 (isActive = false)

    return ResponseEntity.ok(CommonApiResponse.noContent());
  }
}
