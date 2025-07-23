package com.honlife.core.app.controller.admin.category;

import com.honlife.core.app.controller.admin.category.payload.AdminCategoryRequest;
import com.honlife.core.app.controller.admin.category.payload.AdminCategoryResponse;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.dto.CategoryDTO;
import com.honlife.core.app.model.category.service.AdminCategoryService;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping(value = "/api/v1/admin/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    public AdminCategoryController(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    /**
     * 기본 카테고리 조회 API
     * @return List<AdminCategoryResponse>
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<AdminCategoryResponse>>> getAllDefaultCategories() {
        List<AdminCategoryResponse> defaultCategories =  adminCategoryService.findAllDefaultCategory().stream().map(
            AdminCategoryResponse::fromDTO
        ).toList();

        return ResponseEntity.ok(CommonApiResponse.success(defaultCategories));
    }

    /**
     * 기본 카테고리 생성 API
     *
     * @param request 생성할 카테고리의 정보
     * @return
     */
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
    @PutMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> updateCategory(
        @PathVariable(name = "id")
        final Long categoryId,
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
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> deleteCategory(
        @PathVariable(name = "id")
        final Long categoryId) {
        if (categoryId != 1L && categoryId != 2L && categoryId != 3L) {
            return ResponseEntity.status(ResponseCode.NOT_FOUND_CATEGORY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_CATEGORY));
        }
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

}
