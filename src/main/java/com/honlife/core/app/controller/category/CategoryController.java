package com.honlife.core.app.controller.category;

import com.honlife.core.app.controller.category.payload.CategoryResponse;
import com.honlife.core.app.controller.category.payload.CategorySaveRequest;
import com.honlife.core.app.controller.category.payload.CategoryWithParentResponse;
import com.honlife.core.app.controller.category.wrapper.CategoryWrapper;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.dto.CategoryDTO;
import com.honlife.core.app.model.routine.service.RoutineService;
import com.honlife.core.infra.error.exceptions.ReferencedException;
import com.honlife.core.infra.error.exceptions.ReferencedWarning;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.honlife.core.app.model.category.service.CategoryService;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private final CategoryService categoryService;
    private final RoutineService routineService;

    /**
     * 카테고리 조회 API
     * 전체 조회 시 대분류 카테고리와 동시에 해당 카테고리의 소분류 카테고리도 함께 반환됩니다.
     * @return CategoryWrapper
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<CategoryWrapper>> getCategories(
        @AuthenticationPrincipal UserDetails userDetails

    ) {
        String userEmail = userDetails.getUsername();

        // 전체 카테고리 찾기
        List<CategoryDTO> categories = categoryService.getCategories(userEmail);

        List<CategoryResponse> categoryResponses = categories.stream().map(
            CategoryResponse::fromDTO
        ).toList();

        return ResponseEntity.ok(CommonApiResponse.success(new CategoryWrapper(categoryResponses)));
    }


    /**
     * 카테고리 단건 조회 API
     * 대분류 카테고리 조회 시 이를 참조하는 소분류 카테고리도 함께 조회 가능합니다.
     * @param categoryId 카테고리 아이디.
     * @return CategoryWithParentResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<CategoryWithParentResponse>> getCategoryById(
        @PathVariable(name="id")
        final Long categoryId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userEmail = userDetails.getUsername();
        CategoryDTO category = categoryService.findCategoryById(categoryId, userEmail);

        return ResponseEntity.ok(CommonApiResponse.success(CategoryWithParentResponse.fromDTO(category)));

    }

    /**
     * 카테고리 생성 API
     * @param categorySaveRequest 생성할 카테고리의 정보
     * @return
     */
    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> createCategory(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody @Valid final CategorySaveRequest categorySaveRequest) {
        // SUB 카테고리지만 부모 카테고리 정보가 없는 경우
        if(categorySaveRequest.getCategoryType() == CategoryType.SUB && categorySaveRequest.getParentId() == null) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        String userEmail = userDetails.getUsername();
        categoryService.createCategory(categorySaveRequest, userEmail);

        return ResponseEntity.ok(CommonApiResponse.success(ResponseCode.CATEGORY_CREATED));
    }

    /**
     * 카테고리 수정 API
     * @param categoryId 수정할 카테고리 id
     * @param bindingResult validation
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> updateCategory(
        @PathVariable(name = "id")
        final Long categoryId,
        @RequestBody @Valid final CategorySaveRequest categorySaveRequest,
        BindingResult bindingResult,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        // SUB 카테고리지만 부모 카테고리 정보가 없는 경우
        if(categorySaveRequest.getCategoryType().equals(CategoryType.SUB) && (categorySaveRequest.getParentId() == null)) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        String userEmail = userDetails.getUsername();
        categoryService.updateCategory(categoryId, userEmail,categorySaveRequest);

        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 카테고리 삭제 API
     * @param categoryId 삭제할 카테고리 id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> deleteCategory(
        @PathVariable(name="id")
        final Long categoryId,
        @AuthenticationPrincipal UserDetails userDetails
        ){

        String userEmail = userDetails.getUsername();

        categoryService.softDrop(categoryId, userEmail);

        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

}