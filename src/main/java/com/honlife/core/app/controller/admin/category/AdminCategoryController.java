package com.honlife.core.app.controller.admin.category;

import com.honlife.core.app.controller.admin.category.payload.AdminCategoryRequest;
import com.honlife.core.app.controller.admin.category.payload.AdminCategoryResponse;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "관리자 카테고리", description = "관리자가 사용하는 카테고리 관리용 API입니다.")
@RestController
@RequestMapping(value = "/api/v1/admin/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminCategoryController {

    /**
     * 기본 카테고리 수정 API
     * @return List<CategoryResponse>
     */
    @Operation(summary = "카테고리 목록 조회", description = "모든 기본 카테고리를 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<AdminCategoryResponse>>> getAllCategories() {
        List<AdminCategoryResponse> response = new ArrayList<>();
        response.add(AdminCategoryResponse.builder()
            .categoryId(1L)
            .categoryName("청소")
            .categoryType(CategoryType.MAJOR)
            .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
            .createTime(LocalDateTime.parse("2025-07-13T21:30:00"))
            .build());
        response.add(AdminCategoryResponse.builder()
            .categoryId(2L)
            .categoryName("요리")
            .categoryType(CategoryType.MAJOR)
            .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
            .createTime(LocalDateTime.parse("2025-07-13T21:30:00"))
            .build());

        return ResponseEntity.ok(CommonApiResponse.success(response));
    }

    /**
     * 기본 카테고리 생성 API
     *
     * @param request 생성할 카테고리의 정보
     * @return
     */
    @Operation(summary = "카테고리 생성", description = "기본 카테고리를 생성합니다.")
    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> createCategory(
        @RequestBody @Valid AdminCategoryRequest request
    ) {
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 카테고리 수정 API
     *
     * @param categoryId 수정할 카테고리 id
     * @param request    생성할 카테고리의 정보
     * @return
     */
    @Operation(summary = "카테고리 수정", description = "카테고리 이름 또는 타입을 수정합니다. id는 1,2,3 중 하나만 가능합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> updateCategory(
        @PathVariable(name = "id")
        @Schema(description = "카테고리 id", example = "3") final Long categoryId,
        @RequestBody @Valid final AdminCategoryRequest request
    ) {
        if (categoryId != 1L && categoryId != 2L && categoryId != 3L) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_CATEGORY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_CATEGORY));
        }
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 카테고리 삭제 API
     *
     * @param categoryId 삭제할 카테고리 id
     * @return
     */
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다. id는 1,2,3 중 하나만 가능합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> deleteCategory(
        @PathVariable(name = "id")
        @Schema(description = "카테고리 id", example = "1") final Long categoryId) {
        if (categoryId != 1L && categoryId != 2L && categoryId != 3L) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_CATEGORY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_CATEGORY));
        }
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

}
