package com.honlife.core.app.controller.admin.item;

import com.honlife.core.app.controller.admin.item.payload.AdminItemRequest;
import com.honlife.core.app.controller.admin.item.payload.AdminItemResponse;
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

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
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
     * 아이템 추가 요청 처리 API
     * @param adminItemRequest 아이템 정보 객체
     * @return 성공시 {@code 200}을 반환합니다.
     */
    @Operation(
            summary = "아이템 추가",
            description = "아이템을 추가합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdminItemRequest.class)
                    )
            )
    )
    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> createItem(
            @RequestBody @Valid AdminItemRequest adminItemRequest
    ) {
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

