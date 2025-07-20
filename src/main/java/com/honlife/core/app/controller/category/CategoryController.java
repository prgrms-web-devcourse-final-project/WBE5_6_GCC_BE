package com.honlife.core.app.controller.category;

import com.honlife.core.app.controller.category.payload.CategoryResponse;
import com.honlife.core.app.controller.category.payload.CategorySaveRequest;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CommonApiResponse<List<CategoryResponse>>> getCategories(
        @RequestParam(required = false) String majorName
    ) {
        if(majorName ==null){
            // name이 넘어오지 않는다면 Authentication에서 유저 아이디 가져와서 해당하는 카테고리 찾아 리턴
            List<CategoryResponse> response = new ArrayList<>();
            // 기본 제공 카테고리
            response.add(CategoryResponse.builder()
                .categoryId(1L)
                .memberId(null)
                .categoryName("청소 / 정리")
                .emoji("") //이모지를 선택하지 않음
                .categoryType(CategoryType.MAJOR)
                .parentId(null)
                .parentName(null)
                .build());
            response.add(CategoryResponse.builder()
                .categoryId(2L)
                .memberId(null)
                .categoryName("요리")
                .emoji("\uD83C\uDF73")
                .categoryType(CategoryType.MAJOR)
                .parentId(null)
                .parentName(null)
                .build());
            // 사용자 정의 카테고리
            response.add(CategoryResponse.builder()
                .categoryId(3L)
                .memberId(1L)
                .categoryName("화장실 청소")
                .emoji("\uD83D\uDEBD")
                .categoryType(CategoryType.SUB)
                .parentId(1L)
                .parentName("청소 / 정리")
                .build());

            return ResponseEntity.ok(CommonApiResponse.success(response));
        }
        // name으로 넘어온 게 있다면 유저의 카테고리 중에서 해당하는 이름 리턴
        switch (majorName) {
            // 소분류 카테고리가 있는 경우
            case "청소 / 정리" -> {
                List<CategoryResponse> response = new ArrayList<>();
                response.add(CategoryResponse.builder()
                    .categoryId(3L)
                    .memberId(1L)
                    .categoryName("화장실 청소")
                    .emoji("\uD83D\uDEBD")
                    .categoryType(CategoryType.SUB)
                    .parentId(1L)
                    .parentName("청소 / 정리")
                    .build());
                return ResponseEntity.ok(CommonApiResponse.success(response));
            }
            // 소분류 카테고리가 없는 경우
            case "요리" -> {
                List<CategoryResponse> response = new ArrayList<>();
                return ResponseEntity.ok(CommonApiResponse.success(response));
            }
            case "화장실 청소" -> {
                List<CategoryResponse> response = new ArrayList<>();
                return ResponseEntity.ok(CommonApiResponse.success(response));
            }
            // 해당하는 카테고리가 없을 경우
            default -> {
                return ResponseEntity.status(ResponseCode.NOT_FOUND_CATEGORY.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_CATEGORY));
            }
        }
    }


    /**
     * 카테고리 특정 조회 API
     * @param categoryId 카테고리 아이디.
     * @return CategoryResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<CategoryResponse>> getCategory(
        @PathVariable(name="id")
        final Long categoryId
    ) {
        if(categoryId ==1L){
            CategoryResponse response = CategoryResponse.builder()
                .categoryId(1L)
                .memberId(null)
                .categoryName("청소 / 정리")
                .emoji("")
                .categoryType(CategoryType.MAJOR)
                .parentId(null)
                .parentName(null)
                .build();
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }
        if(categoryId ==2L){
            CategoryResponse response = CategoryResponse.builder()
                .categoryId(2L)
                .memberId(null)
                .categoryName("요리")
                .emoji("\uD83C\uDF73")
                .categoryType(CategoryType.MAJOR)
                .parentId(null)
                .parentName(null)
                .build();
            return ResponseEntity.ok(CommonApiResponse.success(response));
        }
        if(categoryId ==3L){
            CategoryResponse response = CategoryResponse.builder()
                .categoryId(3L)
                .memberId(1L)
                .categoryName("화장실 청소")
                .emoji("\uD83D\uDEBD")
                .categoryType(CategoryType.SUB)
                .parentId(1L)
                .parentName("청소 / 정리")
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
    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> createCategory(@RequestBody @Valid final CategorySaveRequest categorySaveRequest,
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

        return ResponseEntity.ok(CommonApiResponse.noContent());
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
        categoryService.delete(categoryId);
        // 존재하지 않는 카테고리 아이디로 접근
        if(categoryId != 1L && categoryId != 2L && categoryId != 3L){
            return ResponseEntity
                .status(ResponseCode.NOT_FOUND_CATEGORY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_CATEGORY));
        }

        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

}