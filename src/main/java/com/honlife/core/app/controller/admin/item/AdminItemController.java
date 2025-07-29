package com.honlife.core.app.controller.admin.item;

import com.honlife.core.app.controller.admin.item.payload.AdminCreateItemRequest;
import com.honlife.core.app.controller.admin.item.payload.AdminItemRequest;
import com.honlife.core.app.controller.admin.item.payload.AdminItemResponse;
import com.honlife.core.app.model.item.service.AdminItemService;
import com.honlife.core.infra.response.CommonApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(value = "/api/v1/admin/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminItemController {

    private final AdminItemService adminItemService;

    /**
     * 관리자 - 전체 아이템 조회 API
     * 모든 아이템 정보를 조회하여 관리자 상점 페이지 등에 사용됩니다.
     * isActive, isListed 여부와 관계없이 전체 아이템을 반환합니다.
     * @return CommonApiResponse<List<AdminItemResponse>> - 전체 아이템 응답 리스트
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<AdminItemResponse>>> getItems(
    ) {
        List<AdminItemResponse> responseList = AdminItemResponse.fromDTOList(adminItemService.getAllItems());
        return ResponseEntity.ok(CommonApiResponse.success(responseList));
    }

    /**
     * 관리자 아이템 생성 API
     *
     * @param request 아이템 생성 요청 정보(AdminCreateItemRequest)
     * @return 성공 시 204 No Content 반환
     */
    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> createItem(
            @RequestBody @Valid AdminCreateItemRequest request
    ) {
        adminItemService.createItem(request);
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 관리자 - 아이템 수정 API
     *
     * @param itemId 수정 대상 아이템의 고유 키
     * @param request 수정할 아이템 정보 (이름,설명,타입,[선택](설명,활성/비 활성화))
     * @return 204 No Content
     * <p><b>[설명]</b></p>
     * - 관리자 페이지에서 아이템을 수정할 때 사용됩니다.
     * - 수정 가능한 항목: 아이템 이름, 설명, 가격, 타입,활성화,비활성화
     * - 아이템 고유키(itemKey)를 기준으로 기존 데이터를 조회한 후 값 갱신
     * - isActive 상태 필드는 별도 API에서 관리
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> updateItem(
        @PathVariable("id") Long itemId,
        @RequestBody AdminItemRequest request
    ) {
        adminItemService.updateItem(itemId, request);
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 관리자 전용 아이템 삭제(Soft Delete) API
     * 해당 itemKey를 가진 아이템의 isActive 값을 false로 변경하여
     * 사용자 단에서는 보이지 않도록 처리합니다.
     *
     * @param itemId 삭제할 아이템의 고유 키
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> deleteItem(
            @PathVariable("id") Long itemId
    ) {
        adminItemService.softDeleteItem(itemId);
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }
}

