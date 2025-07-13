package com.honlife.core.app.controller.admin.point;

import com.honlife.core.app.controller.admin.point.payload.AdminPointPoliciesResponse;
import com.honlife.core.app.controller.admin.point.payload.AdminPointPolicyDetailResponse;
import com.honlife.core.app.controller.admin.point.payload.AdminPointPolicyRequest;
import com.honlife.core.app.model.point.code.PointSourceType;
import com.honlife.core.app.model.point.service.PointPolicyService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
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


@Tag(name = "관리자 - 포인트 정책", description = "관리자용 포인트 정책 관리 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/admin/points/policies", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class PointPolicyController {

    private final PointPolicyService pointPolicyService;

    public PointPolicyController(final PointPolicyService pointPolicyService) {
        this.pointPolicyService = pointPolicyService;
    }

    /**
     * 포인트 정책 목록 조회 API
     * @param pointSourceType 포인트 소스 타입 (선택사항)
     * @return AdminPointPoliciesResponse
     */
    @Operation(
        summary = "포인트 정책 목록 조회",
        description = "관리자가 설정한 모든 포인트 정책 목록을 조회합니다.<br><br>" +
            "<strong>권한:</strong> 관리자만 접근 가능<br><br>" +
            "<strong>파라미터:</strong><br>" +
            "• type 있음: 해당 타입의 정책만 반환<br>" +
            "• type 없음: 모든 타입의 정책 반환<br><br>" +
            "<strong>응답 데이터:</strong><br>" +
            "• 모든 포인트 정책 목록 (활성/비활성 포함)<br>" +
            "• 포인트 소스 타입별 정책<br>" +
            "• 참조 키 및 지급 포인트 정보<br>" +
            "• 생성/수정 일시 포함<br><br>" +
            "<strong>포인트 소스 타입:</strong><br>" +
            "• ROUTINE: 루틴 완료 관련 포인트<br>" +
            "• WEEKLY: 주간 퀘스트 관련 포인트<br>" +
            "• EVENT: 이벤트 퀘스트 관련 포인트<br>" +
            "• CHALLENGE: 도전과제/배지 관련 포인트<br><br>" +
            "*실제 DB에 반영되지 않음*"
    )
    @GetMapping
    public ResponseEntity<CommonApiResponse<AdminPointPoliciesResponse>> getAllPointPolicies(
        @RequestParam(value = "type", required = false)
        @Schema(description = "포인트 소스 타입 (선택사항)", example = "ROUTINE") PointSourceType pointSourceType
    ) {
        // 모킹 데이터 생성
        AdminPointPoliciesResponse response = new AdminPointPoliciesResponse();
        List<AdminPointPoliciesResponse.PolicyItem> policies = new ArrayList<>();

        if (pointSourceType == null) {
            // 전체 타입 정책 반환
            policies.add(AdminPointPoliciesResponse.PolicyItem.builder()
                .id(1L)
                .type(PointSourceType.ROUTINE)
                .referenceKey("ROUTINE_COMPLETE")
                .point(10)
                .isActive(true)
                .createdAt(LocalDateTime.of(2025, 1, 10, 9, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 10, 9, 0))
                .build());
            policies.add(AdminPointPoliciesResponse.PolicyItem.builder()
                .id(2L)
                .type(PointSourceType.WEEKLY)
                .referenceKey("W_COMPLETE_10_ROUTINES")
                .point(100)
                .isActive(true)
                .createdAt(LocalDateTime.of(2025, 1, 11, 10, 30))
                .updatedAt(LocalDateTime.of(2025, 1, 11, 10, 30))
                .build());
        } else if (pointSourceType == PointSourceType.ROUTINE) {
            // 루틴 관련 정책만
            policies.add(AdminPointPoliciesResponse.PolicyItem.builder()
                .id(1L)
                .type(PointSourceType.ROUTINE)
                .referenceKey("ROUTINE_COMPLETE")
                .point(10)
                .isActive(true)
                .createdAt(LocalDateTime.of(2025, 1, 10, 9, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 10, 9, 0))
                .build());
        } else if (pointSourceType == PointSourceType.WEEKLY) {
            // 주간 퀘스트 정책만
            policies.add(AdminPointPoliciesResponse.PolicyItem.builder()
                .id(2L)
                .type(PointSourceType.WEEKLY)
                .referenceKey("W_COMPLETE_10_ROUTINES")
                .point(100)
                .isActive(true)
                .createdAt(LocalDateTime.of(2025, 1, 11, 10, 30))
                .updatedAt(LocalDateTime.of(2025, 1, 11, 10, 30))
                .build());
        }

        response.setPolicies(policies);

        // 실제 구현 시에는 다음과 같은 로직 수행:
        // 1. pointSourceType이 있으면 해당 타입의 정책만 조회
        // 2. pointSourceType이 없으면 모든 정책 조회 (활성/비활성 포함)
        // 3. 생성일시 기준 정렬
        // 4. DTO 변환하여 반환

        return ResponseEntity.ok(CommonApiResponse.success(response));
    }

    /**
     * 특정 포인트 정책 조회 API
     * @param policyId 정책 ID
     * @return AdminPointPolicyDetailResponse
     */
    @Operation(
        summary = "특정 포인트 정책 조회",
        description = "특정 포인트 정책의 상세 정보를 조회합니다.<br><br>" +
            "<strong>권한:</strong> 관리자만 접근 가능<br><br>" +
            "*실제 DB에 반영되지 않음*"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<AdminPointPolicyDetailResponse>> getPointPolicy(
        @PathVariable(name = "id")
        @Schema(description = "정책 ID", example = "1") final Long policyId
    ) {
        // 존재하지 않는 정책 ID로 접근
        if (policyId != 1L && policyId != 2L) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_POLICY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_POLICY));
        }

        // 모킹 데이터 생성
        AdminPointPolicyDetailResponse response = AdminPointPolicyDetailResponse.builder()
            .id(policyId)
            .type(PointSourceType.ROUTINE)
            .referenceKey("ROUTINE_COMPLETE")
            .point(10)
            .isActive(true)
            .createdAt(LocalDateTime.of(2025, 1, 10, 9, 0))
            .updatedAt(LocalDateTime.of(2025, 1, 10, 9, 0))
            .build();

        return ResponseEntity.ok(CommonApiResponse.success(response));
    }

    /**
     * 포인트 정책 생성 API
     * @param request 정책 생성 요청 정보
     * @param bindingResult validation
     * @return
     */
    @Operation(
        summary = "포인트 정책 생성",
        description = "새로운 포인트 정책을 생성합니다.<br><br>" +
            "<strong>권한:</strong> 관리자만 접근 가능<br><br>" +
            "<strong>필수 필드:</strong><br>" +
            "• type: 포인트 소스 타입 (ROUTINE, WEEKLY, EVENT, CHALLENGE)<br>" +
            "• referenceKey: 참조 키 (고유한 식별자)<br>" +
            "• point: 지급할 포인트 (양수)<br><br>" +
            "<strong>선택 필드:</strong><br>" +
            "• isActive: 활성화 여부 (기본값: true)<br><br>" +
            "<strong>예시 참조 키:</strong><br>" +
            "• ROUTINE_COMPLETE: 루틴 완료 시<br>" +
            "• STREAK_7_DAYS: 7일 연속 달성 시<br>" +
            "• W_COMPLETE_10_ROUTINES: 주간 퀘스트<br>" +
            "• E_NEW_YEAR_CHALLENGE: 이벤트 퀘스트<br>" +
            "• BADGE_GOLD_CLEANER: 금 등급 배지 획득 시<br><br>" +
            "*실제 DB에 반영되지 않음*"
    )
    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> createPointPolicy(
        @RequestBody @Valid final AdminPointPolicyRequest request,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        // 실제 구현 시에는 다음과 같은 로직 수행:
        // 1. referenceKey 중복 검사
        // 2. 포인트 양수 검증 (Validation으로 처리됨)
        // 3. AdminPointPolicySaveRequest를 PointPolicyDTO로 변환
        // 4. pointPolicyService.create() 호출

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CommonApiResponse.noContent());
    }

    /**
     * 포인트 정책 수정 API
     * @param policyId 수정할 정책 ID
     * @param request 정책 수정 요청 정보
     * @param bindingResult validation
     * @return
     */
    @Operation(
        summary = "포인트 정책 수정",
        description = "특정 포인트 정책을 수정합니다.<br><br>" +
            "<strong>권한:</strong> 관리자만 접근 가능<br><br>" +
            "<strong>수정 가능 필드:</strong><br>" +
            "• type: 포인트 소스 타입 변경<br>" +
            "• referenceKey: 참조 키 변경<br>" +
            "• point: 지급 포인트 변경<br>" +
            "• isActive: 활성화 상태 변경<br><br>" +
            "<strong>주의사항:</strong><br>" +
            "• 이미 지급된 포인트에는 영향을 주지 않습니다<br>" +
            "• referenceKey 변경 시 기존 참조와의 연결이 끊어집니다<br><br>" +
            "*실제 DB에 반영되지 않음*"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> updatePointPolicy(
        @PathVariable(name = "id")
        @Schema(description = "정책 ID", example = "1") final Long policyId,
        @RequestBody @Valid final AdminPointPolicyRequest request,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        // 존재하지 않는 정책 ID로 접근
        if (policyId != 1L && policyId != 2L) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_POLICY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_POLICY));
        }

        // 실제 구현 시에는 다음과 같은 로직 수행:
        // 1. policyId 존재 여부 검증
        // 2. referenceKey 중복 검사 (다른 정책과)
        // 3. 포인트 양수 검증 (Validation으로 처리됨)
        // 4. AdminPointPolicySaveRequest를 PointPolicyDTO로 변환
        // 5. pointPolicyService.update() 호출

        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 포인트 정책 삭제 API
     * @param policyId 삭제할 정책 ID
     * @return
     */
    @Operation(
        summary = "포인트 정책 삭제",
        description = "특정 포인트 정책을 삭제합니다.<br><br>" +
            "<strong>권한:</strong> 관리자만 접근 가능<br><br>" +
            "<strong>주의사항:</strong><br>" +
            "• 삭제된 정책은 복구할 수 없습니다<br>" +
            "• 이미 지급된 포인트에는 영향을 주지 않습니다<br>" +
            "• 해당 정책을 참조하는 새로운 포인트 지급이 중단됩니다<br><br>" +
            "<strong>권장사항:</strong><br>" +
            "• 완전 삭제보다는 isActive를 false로 설정하는 것을 권장<br><br>" +
            "*실제 DB에 반영되지 않음*"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> deletePointPolicy(
        @PathVariable(name = "id")
        @Schema(description = "정책 ID", example = "1") final Long policyId
    ) {
        // 존재하지 않는 정책 ID로 접근
        if (policyId != 1L && policyId != 2L) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_POLICY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_POLICY));
        }

        // 실제 구현 시에는 다음과 같은 로직 수행:
        // 1. policyId 존재 여부 검증
        // 2. 참조 관계 확인 (현재 사용 중인 정책인지)
        // 3. pointPolicyService.delete() 호출
        // 4. soft delete 권장 (isActive = false)

        return ResponseEntity.ok(CommonApiResponse.noContent());
    }
}
