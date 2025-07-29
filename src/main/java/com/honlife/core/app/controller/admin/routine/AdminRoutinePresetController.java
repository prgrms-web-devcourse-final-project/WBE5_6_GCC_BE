package com.honlife.core.app.controller.admin.routine;

import com.honlife.core.app.controller.admin.routine.payload.AdminRoutinePresetDetailResponse;
import com.honlife.core.app.controller.admin.routine.payload.AdminRoutinePresetSaveRequest;
import com.honlife.core.app.controller.admin.routine.payload.AdminRoutinePresetsResponse;
import com.honlife.core.app.controller.routine.payload.RoutinePresetsResponse;
import com.honlife.core.app.model.routine.code.RepeatType;
import com.honlife.core.app.model.routine.service.RoutinePresetService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

@Tag(name = "🔄 [관리자] 추천 루틴", description = "관리자용 추천 루틴 관리 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/admin/routines/presets", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoutinePresetController {

  private final RoutinePresetService routinePresetService;

  public AdminRoutinePresetController(final RoutinePresetService routinePresetService) {
    this.routinePresetService = routinePresetService;
  }

  /**
   * 추천 루틴 프리셋 목록 조회 API
   * @return AdminRoutinePresetsResponse
   */
  @Operation(
      summary = "🔄 추천 루틴 프리셋 목록 조회",
      description = "관리자가 등록한 모든 추천 루틴 프리셋 목록을 조회합니다.<br><br>" +
          "<strong>권한:</strong> 관리자만 접근 가능<br><br>" +
          "<strong>응답 데이터:</strong><br>" +
          "• 모든 프리셋 목록 (활성/비활성 포함)<br>" +
          "• 카테고리 이름 포함<br>" +
          "• 생성/수정 일시 포함<br>" +
          "• 활성화 상태 표시<br><br>" +
          "*실제 DB에 반영되지 않음*"
  )
  @GetMapping
  public ResponseEntity<CommonApiResponse<AdminRoutinePresetsResponse>> getAllRoutinePresets(
      @RequestParam(value = "id", required = false)
      @Schema(description = "카테고리 ID (선택사항)", example = "1") Long categoryId
  ) {
    // 모킹 데이터 생성
    AdminRoutinePresetsResponse response = new AdminRoutinePresetsResponse();
    List<AdminRoutinePresetsResponse.PresetItem> presets = new ArrayList<>();

    if (categoryId == 1L) {
      // 청소 카테고리 프리셋
      //추천 루틴은 주기까지 추가할필요 없을거같아서 추가하지 않았습니다
      presets.add(AdminRoutinePresetsResponse.PresetItem.builder()
          .presetId(1L)
          .categoryId(1L)
          .majorCategory("청소")
          .name("화장실 청소하기")
          .triggerTime("23:00")
          .isImportant(true)
          .updatedAt(LocalDate.of(2025, 8, 1).atStartOfDay())
          .createdAt(LocalDate.of(2025, 6, 1).atStartOfDay())
          .repeatTerm(3)
          .initDate(LocalDate.of(2025, 8, 1))
          .repeatType(RepeatType.WEEKLY)
          .repeatValue("1,3,5")
          .emoji("✅")
          .build());

      presets.add(AdminRoutinePresetsResponse.PresetItem.builder()
          .presetId(2L)
          .categoryId(1L)
          .majorCategory("청소")
          .name("청소기 돌리기")
          .triggerTime("21:00")
          .isImportant(true)
          .updatedAt(LocalDate.of(2025, 7, 1).atStartOfDay())
          .createdAt(LocalDate.of(2025, 6, 1).atStartOfDay())
          .repeatTerm(3)
          .initDate(LocalDate.of(2025, 8, 1))
          .repeatType(RepeatType.WEEKLY)
          .repeatValue("1,3,5")
          .emoji("✅")
          .build());

      presets.add(AdminRoutinePresetsResponse.PresetItem.builder()
          .presetId(3L)
          .categoryId(1L)
          .majorCategory("청소")
          .name("식탁 닦기")
          .triggerTime("20:00")
          .isImportant(false)
          .updatedAt(LocalDate.of(2025, 7, 1).atStartOfDay())
          .createdAt(LocalDate.of(2025, 6, 1).atStartOfDay())
          .repeatTerm(1)
          .initDate(LocalDate.of(2025, 8, 1))
          .repeatType(RepeatType.DAILY)
          .repeatValue(null)
          .emoji("✅")
          .build());

    } else if (categoryId == 2L) {
      // 건강 카테고리 프리셋
      presets.add(AdminRoutinePresetsResponse.PresetItem.builder()
          .presetId(4L)
          .categoryId(2L)
          .majorCategory("건강")
          .name("명상하기")
          .triggerTime("22:00")
          .isImportant(true)
          .updatedAt(LocalDate.of(2025, 7, 1).atStartOfDay())
          .createdAt(LocalDate.of(2025, 6, 1).atStartOfDay())
          .repeatTerm(3)
          .initDate(LocalDate.of(2025, 7, 1))
          .repeatType(RepeatType.WEEKLY)
          .repeatValue("4,5")
          .emoji("✅")
          .build());

      presets.add(AdminRoutinePresetsResponse.PresetItem.builder()
          .presetId(5L)
          .categoryId(2L)
          .majorCategory("건강")
          .name("물 마시기")
          .triggerTime("08:00")
          .isImportant(true)
          .updatedAt(LocalDate.of(2025, 7, 1).atStartOfDay())
          .createdAt(LocalDate.of(2025, 6, 1).atStartOfDay())
          .repeatTerm(2)
          .initDate(LocalDate.of(2025, 7, 1))
          .repeatType(RepeatType.WEEKLY)
          .repeatValue("4,5")
          .emoji("✅")
          .build());

    } else if (categoryId == 4L) {
      // 운동 카테고리 프리셋
      presets.add(AdminRoutinePresetsResponse.PresetItem.builder()
          .presetId(6L)
          .categoryId(4L)
          .majorCategory("운동")
          .name("아침 스트레칭")
          .triggerTime("07:30")
          .isImportant(true)
          .updatedAt(LocalDate.of(2025, 7, 1).atStartOfDay())
          .createdAt(LocalDate.of(2025, 6, 1).atStartOfDay())
          .repeatTerm(3)
          .initDate(LocalDate.of(2025, 5, 1))
          .repeatType(RepeatType.DAILY)
          .repeatValue(null)
          .emoji("✅")
          .build());

      presets.add(AdminRoutinePresetsResponse.PresetItem.builder()
          .presetId(7L)
          .categoryId(4L)
          .majorCategory("운동")
          .name("플랭크")
          .triggerTime("18:00")
          .isImportant(false)
          .updatedAt(LocalDate.of(2025, 7, 1).atStartOfDay())
          .createdAt(LocalDate.of(2025, 6, 1).atStartOfDay())
          .repeatTerm(3)
          .initDate(LocalDate.of(2025, 8, 1))
          .repeatType(RepeatType.WEEKLY)
          .repeatValue("1,3,5")
          .emoji("✅")
          .build());

      presets.add(AdminRoutinePresetsResponse.PresetItem.builder()
          .presetId(8L)
          .categoryId(4L)
          .majorCategory("운동")
          .name("계단 오르기")
          .triggerTime("출근시간")
          .isImportant(false)
          .updatedAt(LocalDate.of(2025, 7, 1).atStartOfDay())
          .createdAt(LocalDate.of(2025, 6, 1).atStartOfDay())
          .repeatTerm(3)
          .initDate(LocalDate.of(2025, 8, 1))
          .repeatType(RepeatType.WEEKLY)
          .repeatValue("1,3,5")
          .emoji("✅")
          .build());
    }

    response.setPresets(presets);

    // 실제 구현 시에는 다음과 같은 로직 수행:
    // 1. categoryId가 있으면 해당 카테고리의 프리셋만 조회
    // 2. categoryId가 없으면 모든 프리셋 조회 (활성/비활성 포함)
    // 3. Category 정보 조인하여 카테고리명 조회
    // 4. 생성일시 기준 정렬
    // 5. DTO 변환하여 반환

    return ResponseEntity.ok(CommonApiResponse.success(response));
  }

  /**
   * 특정 추천 루틴 프리셋 조회 API
   * @param presetId 프리셋 ID
   * @return AdminRoutinePresetDetailResponse
   */
  @Operation(
      summary = "🔄 특정 추천 루틴 프리셋 조회",
      description = "특정 추천 루틴 프리셋의 상세 정보를 조회합니다.<br><br>" +
          "<strong>권한:</strong> 관리자만 접근 가능<br><br>" +
          "*실제 DB에 반영되지 않음*"
  )
  @GetMapping("/{id}")
  public ResponseEntity<CommonApiResponse<AdminRoutinePresetDetailResponse>> getRoutinePreset(
      @PathVariable(name = "id")
      @Schema(description = "프리셋 ID", example = "1") final Long presetId
  ) {
    // 존재하지 않는 프리셋 ID로 접근
    if (presetId != 1L && presetId != 2L && presetId != 3L) {
      return ResponseEntity.status(ResponseCode.NOT_FOUND.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_FOUND));
    }

    // 모킹 데이터 생성
    AdminRoutinePresetDetailResponse response = AdminRoutinePresetDetailResponse.builder()
        .presetId(1L)
        .categoryId(1L)
        .majorCategory("청소")
        .name("변기 청소하기")
        .triggerTime("09:00")
        .isImportant(false)
        .updatedAt(LocalDate.of(2025, 7, 1).atStartOfDay())
        .createdAt(LocalDate.of(2025, 6, 1).atStartOfDay())
        .repeatTerm(3)
        .initDate(LocalDate.of(2025, 8, 1))
        .repeatType(RepeatType.WEEKLY)
        .repeatValue("1,3,5")
        .emoji("✅")
        .build();

    return ResponseEntity.ok(CommonApiResponse.success(response));
  }

  /**
   * 추천 루틴 프리셋 생성 API
   * @param request 프리셋 생성 요청 정보
   * @param bindingResult validation
   * @return
   */
  @Operation(
      summary = "🔄 추천 루틴 프리셋 생성",
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
      summary = "🔄 추천 루틴 프리셋 수정",
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
      summary = "🔄 추천 루틴 프리셋 삭제",
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
