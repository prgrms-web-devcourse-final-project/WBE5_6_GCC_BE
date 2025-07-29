package com.honlife.core.app.controller.category;

import com.honlife.core.app.controller.category.payload.CategoryResponse;
import com.honlife.core.app.controller.category.payload.CategorySaveRequest;
import com.honlife.core.app.controller.category.payload.CategoryWithParentResponse;
import com.honlife.core.app.controller.category.payload.ChildCategoryResponse;
import com.honlife.core.app.controller.category.wrapper.CategoryWrapper;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.category.service.CategoryService;

@SecurityRequirement(name = "bearerAuth")
@Tag(name="✅ [일반] 카테고리", description = "카테고리 관련 API 입니다. <br>요청 바디 중 type에는 MAJOR, SUB가 들어갈 수 있습니다.")
@RestController
@RequestMapping(value = "/api/v1/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 카테고리 조회 API
     * 전체 조회 시 대분류 카테고리와 동시에 해당 카테고리의 소분류 카테고리도 함께 반환됩니다.
     * @return CategoryWrapper
     */
    @Operation(summary = "✅ 카테고리 조회", description = "모든 카테고리를 조회합니다. 해당 카테고리를 찹조하는 소분류 카테고리가 있을 경우 함께 반환됩니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<CategoryWrapper>> getCategories(
    ) {
            List<CategoryResponse> response = new ArrayList<>();
            // 기본 제공 카테고리
            response.add(CategoryResponse.builder()
                .categoryId(1L)
                .categoryName("청소 / 정리")
                .emoji("") //이모지를 선택하지 않음
                .categoryType(CategoryType.MAJOR)
                .children(List.of(ChildCategoryResponse.builder()
                    .categoryId(3L)
                    .categoryName("화장실 청소")
                    .emoji("\uD83D\uDEBD")
                    .categoryType(CategoryType.SUB)
                    .parentId(1L)
                    .build()))
                .build());
            response.add(CategoryResponse.builder()
                .categoryId(2L)
                .categoryName("요리")
                .emoji("\uD83C\uDF73")
                .categoryType(CategoryType.MAJOR)
                .build());

            CategoryWrapper wrapper = new CategoryWrapper(response);

            return ResponseEntity.ok(CommonApiResponse.success(wrapper));
    }


    /**
     * 카테고리 특정 조회 API
     * @param categoryId 카테고리 아이디.
     * @return CategoryResponse
     */
    @Operation(summary = "✅ 특정 카테고리 조회", description = "카테고리 id를 통해 카테고리에 대한 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<CategoryWithParentResponse>> getCategory(
        @PathVariable(name="id")
        @Schema(description="카테고리 id", example = "1")
        final Long categoryId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        if(categoryId ==1L){
            CategoryWithParentResponse response = CategoryWithParentResponse.builder()
                .categoryId(1L)
                .categoryName("청소 / 정리")
                .emoji("")
                .categoryType(CategoryType.MAJOR)
                .children(List.of(ChildCategoryResponse.builder()
                    .categoryId(3L)
                    .categoryName("화장실 청소")
                    .emoji("\uD83D\uDEBD")
                    .categoryType(CategoryType.SUB)
                    .parentId(1L)
                    .build()))
                .parentId(null)
                .build();
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }
        if(categoryId ==2L){
            CategoryWithParentResponse response = CategoryWithParentResponse.builder()
                .categoryId(2L)
                .categoryName("요리")
                .emoji("\uD83C\uDF73")
                .categoryType(CategoryType.MAJOR)
                .parentId(null)
                .build();
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }
        if(categoryId ==3L){
            CategoryWithParentResponse response = CategoryWithParentResponse.builder()
                .categoryId(3L)
                .categoryName("화장실 청소")
                .emoji("\uD83D\uDEBD")
                .categoryType(CategoryType.SUB)
                .parentId(1L)
                .build();
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }
        // 해당하는 카테고리가 없을 경우
        else{
            return ResponseEntity.status(ResponseCode.NOT_FOUND_CATEGORY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_CATEGORY));
        }

    }

    /**
     * 카테고리 생성 API
     * @param categorySaveRequest 생성할 카테고리의 정보
     * @return
     */
    @Operation(summary = "✅ 카테고리 생성", description = "카테고리를 생성합니다. <br>이름과 type은 무조건 작성하여야 합니다. 만약 type이 SUB일 시, 대분류 카테고리에 대한 정보도 필수로 작성하여야 합니다. <br>*실제 DB에 반영되지 않음*")
    @PostMapping
    public ResponseEntity<CommonApiResponse<ResponseCode>> createCategory(@RequestBody @Valid final CategorySaveRequest categorySaveRequest,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }
        if(categorySaveRequest.getCategoryType().equals(CategoryType.SUB) && categorySaveRequest.getParentName() == null){
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        return ResponseEntity.ok(CommonApiResponse.success(ResponseCode.CATEGORY_CREATED));
    }

    /**
     * 카테고리 수정 API
     * @param categoryId 수정할 카테고리 id
     * @param bindingResult validation
     * @return
     */
    @Operation(summary = "✅ 카테고리 수정", description = "특정 카테고리를 수정합니다. <br>id가 1,2,3 인 데이터에 대해서만 수정 요청을 할 수 있도록 하였습니다. <br>*실제 DB에 반영되지 않음*")
    @PatchMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> updateCategory(
        @PathVariable(name = "id")
        @Schema(description = "카테고리 id", example = "3")
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
    @Operation(summary = "✅ 카테고리 삭제", description = "특정 카테고리를 삭제합니다. <br>id가 1,2,3 인 데이터에 대해서만 삭제 요청을 할 수 있도록 하였습니다. <br>*실제 DB에 반영되지 않음*")
    public ResponseEntity<CommonApiResponse<Void>> deleteCategory(
        @PathVariable(name="id")
        @Schema(description = "카테고리 id", example = "1") final Long categoryId){
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
