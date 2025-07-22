package com.honlife.core.app.controller.category;

import com.honlife.core.app.controller.category.payload.CategoryResponse;
import com.honlife.core.app.controller.category.payload.CategorySaveRequest;
import com.honlife.core.app.controller.category.payload.CategoryWithParentResponse;
import com.honlife.core.app.controller.category.wrapper.CategoryWrapper;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.dto.CategoryDTO;
import com.honlife.core.app.model.category.dto.CategoryUserViewDTO;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.category.service.CategoryService;

@RestController
@RequestMapping(value = "/api/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 카테고리 조회 API
     * @param majorName 카테고리 이름이 넘어오지 않으면 소분류 카테고리를, 넘어온다면 해당하는 이름의 카테고리를 전달합니다.
     * @return List<CategoryResponse>
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<CategoryWrapper>> getCategories(
        @RequestParam(required = false) String majorName,
        @AuthenticationPrincipal UserDetails userDetails

    ) {
        String userEmail = userDetails.getUsername();
        List<CategoryDTO> categories = new ArrayList<>();

        if(majorName == null) {
            // 전체 카테고리 찾기
            categories = categoryService.getCategories(userEmail);
        }else{
            // 소분류 카테고리 찾기
            categories = categoryService.getSubCategories(userEmail, majorName);
        }

        List<CategoryResponse> categoryResponses = categories.stream().map(
            CategoryResponse::fromDTO
        ).toList();

        return ResponseEntity.ok(CommonApiResponse.success(new CategoryWrapper(categoryResponses)));
    }


    /**
     * 카테고리 특정 조회 API
     * @param categoryId 카테고리 아이디.
     * @return CategoryResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<CategoryWithParentResponse>> getCategory(
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
        @RequestBody @Valid final CategorySaveRequest categorySaveRequest,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }
        // SUB 카테고리지만 부모 카테고리 정보가 없는 경우
        if(categorySaveRequest.getCategoryType().equals(CategoryType.SUB) && (categorySaveRequest.getParentName() == null|| categorySaveRequest.getParentName().isEmpty())) {
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
    @PutMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> updateCategory(
        @PathVariable(name = "id")
        final Long categoryId,
        @RequestBody @Valid final CategorySaveRequest categorySaveRequest,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }
        // 존재하지 않는 카테고리 아이디로 접근
        if(categoryId != 1L && categoryId != 2L && categoryId != 3L){
            return ResponseEntity
                .status(ResponseCode.NOT_FOUND_CATEGORY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_CATEGORY));
        }

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
        final Long categoryId){
//        final ReferencedWarning referencedWarning = categoryService.getReferencedWarning(id);
//        if (referencedWarning != null) {
//            throw new ReferencedException(referencedWarning);
//        }
//        categoryService.delete(categoryId);
        // 존재하지 않는 카테고리 아이디로 접근
        if(categoryId != 1L && categoryId != 2L && categoryId != 3L){
            return ResponseEntity
                .status(ResponseCode.NOT_FOUND_CATEGORY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_CATEGORY));
        }

        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

}