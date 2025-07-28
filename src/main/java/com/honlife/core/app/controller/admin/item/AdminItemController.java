package com.honlife.core.app.controller.admin.item;

import com.honlife.core.app.controller.admin.item.payload.AdminCreateItemRequeset;
import com.honlife.core.app.controller.admin.item.payload.AdminItemListedRequest;
import com.honlife.core.app.controller.admin.item.payload.AdminItemRequest;
import com.honlife.core.app.controller.admin.item.payload.AdminItemResponse;
import com.honlife.core.app.model.item.code.ItemType;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 관리자 아이템 컨트롤러입니다. 관리자 페이지에서 아이템 조회, 추가, 수정 및 삭제에 사용되는 API 들을 정의합니다.
 */
@RestController
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "✅[관리자] 아이템 관리", description = "관리자 아이템 관련 API 입니다.")
@RequestMapping(value = "/api/v1/admin/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminItemController {

    /**
     * 모든 아이템 조회 요청 처리 API
     *
     * @return 모든 아이템에 대한 리스트를 반환합니다. 만약 특정 타입이 함께 넘어온 경우, 해당 타입의 모든 아이템 리스트가 반환됩니다.
     */
    @Operation(summary = "아이템 전체 조회", description = "아이템의 전체 목록을 조회합니다.<br>"
            + "{type} 에 값을 넣으면, 해당 아이템 타입의 모든 아이템 목록을 조회할 수 있습니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<?>> getItems() {
        List<AdminItemResponse> items = new ArrayList<>();

        items.add(AdminItemResponse.builder()
                .itemId(1L)
                .itemKey("top_item_01")
                .itemName("청소 상의")
                .itemPrice(100)
                .itemType(ItemType.TOP)
                .itemDescription("청소에 도움됩니다.")
                .isListed(true)
                .isActive(true)
                .build());
        items.add(AdminItemResponse.builder()
                .itemId(2L)
                .itemKey("top_item_02")
                .itemName("요리 상의")
                .itemPrice(101)
                .itemType(ItemType.TOP)
                .itemDescription("청소에 도움됩니다.")
                .isListed(true)
                .isActive(true)
                .build());
        return ResponseEntity.ok(CommonApiResponse.success(items));
    }

    /**
     * 아이템 추가 요청 처리 API
     *
     * @param adminCreateItemRequeset 아이템 정보 객체
     * @return 성공시 {@code 200}을 반환합니다.
     */
    @Operation(
            summary = "아이템 추가",
            description = "아이템을 추가합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdminCreateItemRequeset.class)
                    )
            )
    )
    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> createItem(
            @RequestBody @Valid AdminCreateItemRequeset adminCreateItemRequeset
    ) {
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 아이템 수정 처리 API
     *
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
                            description = "수정할 아이템의 id값",
                            required = true,
                            example = "1L"
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
            @PathVariable("id") Long id,
            @RequestBody @Valid AdminItemRequest request
    ) {
        if (id == 1L) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
    }

    /**
     * 아이템 삭제 요청 처리 API
     * 아이템 ID 기반으로 삭제합니다
     * @param id 아이템 id값
     * @return 성공시 {@code 200}을 반환합니다.
     */
    @Operation(
            summary = "아이템 삭제",
            description = "아이템을 삭제합니다.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "삭제할 아이템의 id값",
                            required = true,
                            example = "1L"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> deleteItem(
            @PathVariable("id") Long id
    ) {
        if (id == 1L) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_ITEM));
        }
    }
}
