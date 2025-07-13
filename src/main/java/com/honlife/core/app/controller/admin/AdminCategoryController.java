package com.honlife.core.app.controller.admin;

import com.honlife.core.app.controller.admin.payload.CategoryCreateRequest;
import com.honlife.core.app.controller.category.payload.CategoryResponse;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "관리자 카테고리", description = "관리자가 사용하는 카테고리 관리용 API입니다.")
@RestController
@RequestMapping(value = "/api/v1/admin/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminCategoryController {

    @Operation(summary = "카테고리 목록 조회", description = "모든 관리자 카테고리를 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> response = new ArrayList<>();
        response.add(CategoryResponse.builder()
            .categoryId(1L)
            .memberId(null)
            .categoryName("화장실 청소")
            .categoryType(CategoryType.SUB)
            .parentId(1L)
            .parentName("청소")
            .build());
        response.add(CategoryResponse.builder()
            .categoryId(2L)
            .memberId(null)
            .categoryName("식재료 준비")
            .categoryType(CategoryType.SUB)
            .parentId(2L)
            .parentName("요리")
            .build());

        return ResponseEntity.ok(CommonApiResponse.success(response));
    }

    @Operation(summary = "카테고리 생성", description = "관리자 카테고리를 생성합니다. type에는 SUB만을 입력하여야 합니다." +
            "<br> (사용자에게 예시를 보여주어야 하기 때문에 상위 하위 카테고리가 모두 필요합니다.)")
    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> createCategory(
        @RequestBody CategoryCreateRequest request
    ) {
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    @Operation(summary = "카테고리 수정", description = "카테고리 이름 또는 타입을 수정합니다. id는 1,2,3 중 하나만 가능합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> updateCategory(
            @PathVariable(name = "id")
            @Schema(description = "카테고리 id", example = "3")
            final Long categoryId,
        @RequestBody final CategoryCreateRequest request
    ) {
        if (categoryId != 1L && categoryId != 2L && categoryId != 3L) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_CATEGORY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_CATEGORY));
        }
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다. id는 1,2,3 중 하나만 가능합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> deleteCategory(
            @PathVariable(name="id")
            @Schema(description = "카테고리 id", example = "1") final Long categoryId) {
        if (categoryId != 1L && categoryId != 2L && categoryId != 3L) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_CATEGORY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_CATEGORY));
        }
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

}
