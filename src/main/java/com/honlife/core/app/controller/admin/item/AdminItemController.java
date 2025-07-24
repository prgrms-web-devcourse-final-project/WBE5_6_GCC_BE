package com.honlife.core.app.controller.admin.item;

import com.honlife.core.app.controller.admin.item.payload.AdminCreateItemRequest;
import com.honlife.core.app.controller.admin.item.payload.AdminItemRequest;
import com.honlife.core.app.controller.admin.item.payload.AdminItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.app.model.item.service.AdminItemService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(value = "/api/v1/admin/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminItemController {

    private final AdminItemService adminItemService;

    /**
     * 모든 아이템 조회 요청 처리 API
     * @param itemType 아이템 타입
     * @return 모든 아이템에 대한 리스트를 반환합니다. 만약 특정 타입이 함께 넘어온 경우, 해당 타입의 모든 아이템 리스트가 반환됩니다.
     */
    @Operation(summary = "아이템 전체 조회", description = "아이템의 전체 목록을 조회합니다.<br>"
        + "{type} 에 값을 넣으면, 해당 아이템 타입의 모든 아이템 목록을 조회할 수 있습니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<?>> getItems(
        @Schema(description = "아이템 타입 입니다.", example = "TOP")
        @RequestParam(value = "type", required = false) final ItemType itemType
    ) {
        List<AdminItemResponse> items = new ArrayList<>();
        if(itemType != null && itemType.name().equals("TOP")) {
            items.add(AdminItemResponse.builder()
                .itemId(1L)
                .itemKey("top_item_01")
                .itemName("청소 상의")
                .itemPrice(100)
                .itemType(ItemType.TOP)
                .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
                .updateTime(LocalDateTime.parse("2025-07-13T21:30:00"))
                .build());
            items.add(AdminItemResponse.builder()
                .itemId(2L)
                .itemKey("top_item_02")
                .itemName("요리 상의")
                .itemPrice(101)
                .itemType(ItemType.TOP)
                .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
                .updateTime(LocalDateTime.parse("2025-07-13T21:30:00"))
                .build());
            return ResponseEntity.ok(CommonApiResponse.success(items));
        } else if (itemType != null) {
            return ResponseEntity.internalServerError().body(CommonApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR));
        }
        items.add(AdminItemResponse.builder()
            .itemId(1L)
            .itemKey("top_item_01")
            .itemName("청소 상의")
            .itemPrice(100)
            .itemType(ItemType.TOP)
            .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
            .updateTime(LocalDateTime.parse("2025-07-13T21:30:00"))
            .build());
        items.add(AdminItemResponse.builder()
            .itemId(3L)
            .itemKey("bottom_item_01")
            .itemName("러닝 바지")
            .itemPrice(101)
            .itemType(ItemType.BOTTOM)
            .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
            .updateTime(LocalDateTime.parse("2025-07-13T21:30:00"))
            .build());
        return ResponseEntity.ok(CommonApiResponse.success(items));
    }

    /**
     * 특정 아이템 정보 조회 요청 API
     * @param itemId 특정 아이템 ID
     * @return 성공시 {@code AdminItemResponse} 를 응답객체에 담아 반환합니다.
     */
    @Operation(summary = "특정 아이템 조회", description = "아이템 id를 통해 특정 아이템에 대한 정보를 조회할 수 있습니다.")
    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<?>> getItem(
        @PathVariable(name = "id") final Long itemId
    ) {
        if (itemId == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
        AdminItemResponse itemResponse = AdminItemResponse.builder()
            .itemId(1L)
            .itemKey("top_item_01")
            .itemName("청소 상의")
            .itemPrice(100)
            .itemType(ItemType.TOP)
            .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
            .updateTime(LocalDateTime.parse("2025-07-13T21:30:00"))
            .build();
        return ResponseEntity.ok(CommonApiResponse.success(itemResponse));
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
     * 아이템 수정 처리 API
     * @param id 아이템 식별 id
     * @param request 아이템 정보 객체
     * @return 성공시 {@code 200}을 반환합니다.
     */
    @Operation(
        summary = "아이템 수정",
        description = "아이템을 수정합니다.",
        parameters = {
            @Parameter(
                name = "id",
                description = "수정할 아이템의 ID",
                required = true,
                example = "10"
            )
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = AdminItemRequest.class)
            )
        )
    )
    @PatchMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> updateItem(
        @PathVariable Long id,
        @RequestBody @Valid AdminItemRequest request
    ) {
        if (id == 10L) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
    }

    /**
     * 아이템 삭제 요청 처리 API
     * @param id 아이템 id
     * @return 성공시 {@code 200}을 반환합니다.
     */
    @Operation(
        summary = "아이템 삭제",
        description = "아이템을 삭제합니다.",
        parameters = {
            @Parameter(
                name = "id",
                description = "삭제할 아이템의 ID",
                required = true,
                example = "10"
            )
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> deleteItem(
        @PathVariable Long id
    ) {
        if (id == 10L) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
    }
}
