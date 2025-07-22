package com.honlife.core.app.controller.admin.item;

import com.honlife.core.app.controller.admin.item.payload.AdminItemListedRequest;
import com.honlife.core.app.model.item.service.AdminItemService;
import com.honlife.core.infra.response.CommonApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/admin/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminItemController {

    private final AdminItemService adminItemService;

    /**
     * 관리자 전용 아이템 삭제(Soft Delete) API
     * 해당 itemKey를 가진 아이템의 isActive 값을 false로 변경하여
     * 사용자 단에서는 보이지 않도록 처리합니다.
     *
     * @param itemKey 삭제할 아이템의 고유 키
     */
    @DeleteMapping("/{key}")
    public ResponseEntity<CommonApiResponse<Void>> deleteItem(
            @PathVariable("key") String itemKey
    ) {
        adminItemService.softDeleteItem(itemKey);
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    @PatchMapping("/{itemKey}/listed")
    public ResponseEntity<CommonApiResponse<Void>> updateItemListingStatus(
            @PathVariable String itemKey,
            @RequestBody AdminItemListedRequest request
    ) {
        adminItemService.updateListedStatus(itemKey, request.getIsListed());
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }
}
