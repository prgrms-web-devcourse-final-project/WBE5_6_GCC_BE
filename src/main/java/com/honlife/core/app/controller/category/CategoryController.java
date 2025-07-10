package com.honlife.core.app.controller.category;

import com.honlife.core.app.controller.category.payload.CategoryPayload;
import com.honlife.core.app.controller.category.payload.CategorySavePayload;
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

@SecurityRequirement(name = "bearerAuth")
@Tag(name="카테고리", description = "카테고리 관련 API 입니다. <br>요청 바디 중 type에는 MAJOR, SUB가 들어갈 수 있습니다.")
@RestController
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 카테고리 조회 API
     * @param name 카테고리 이름이 넘어오지 않으면 전체 카테고리를, 넘어온다면 해당하는 이름의 카테고리를 전달합니다.
     * @return List<CategoryPayload>
     */
    @Operation(summary = "카테고리 조회", description = "name 값을 넣지 않으면 전체 조회를, name 값이 있다면 해당하는 카테고리를 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<CategoryPayload>>> getCategories(
        @Schema(name="name", description="카테고리 이름을 적어주세요", example = "청소")
        @RequestParam(required = false) String name
    ) {
        if(name==null){
            // name이 넘어오지 않는다면 Authentication에서 유저 아이디 가져와서 해당하는 카테고리 찾아 리턴
            List<CategoryPayload> response = new ArrayList<>();
            // 기본 제공 카테고리
            response.add(CategoryPayload.builder()
                .id(1L)
                .member(null)
                .name("청소")
                .type(CategoryType.MAJOR)
                .parentName(null)
                .build());
            response.add(CategoryPayload.builder()
                .id(2L)
                .member(null)
                .name("요리")
                .type(CategoryType.MAJOR)
                .parentName(null)
                .build());
            // 사용자 정의 카테고리
            response.add(CategoryPayload.builder()
                .id(3L)
                .member(1L)
                .name("화장실 청소")
                .type(CategoryType.SUB)
                .parentName("청소")
                .build());

            return ResponseEntity.ok(CommonApiResponse.success(response));
        }
        // name으로 넘어온 게 있다면 유저의 카테고리 중에서 해당하는 이름 리턴
        switch (name) {
            case "청소" -> {
                List<CategoryPayload> response = new ArrayList<>();
                response.add(CategoryPayload.builder()
                    .id(1L)
                    .member(null)
                    .name("청소")
                    .type(CategoryType.MAJOR)
                    .parentName(null)
                    .build());
                return ResponseEntity.ok(CommonApiResponse.success(response));
            }
            case "요리" -> {
                List<CategoryPayload> response = new ArrayList<>();
                response.add(CategoryPayload.builder()
                    .id(2L)
                    .member(null)
                    .name("요리")
                    .type(CategoryType.MAJOR)
                    .parentName(null)
                    .build());
                return ResponseEntity.ok(CommonApiResponse.success(response));
            }
            case "화장실 청소" -> {
                List<CategoryPayload> response = new ArrayList<>();
                response.add(CategoryPayload.builder()
                    .id(3L)
                    .member(null)
                    .name("화장실 청소")
                    .type(CategoryType.SUB)
                    .parentName("청소")
                    .build());
                return ResponseEntity.ok(CommonApiResponse.success(response));
            }
            // 해당하는 카테고리가 없을 경우
            default -> {
                return ResponseEntity.status(ResponseCode.NOT_EXIST_CATEGORY.status())
                    .body(CommonApiResponse.error(ResponseCode.NOT_EXIST_CATEGORY));
            }
        }
    }

    /**
     * 카테고리 생성 API
     * @param categorySavePayload 생성할 카테고리의 정보
     * @return
     */
    @Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다. <br>이름과 type은 무조건 작성하여야 합니다. 만약 type이 SUB일 시, 대분류 카테고리에 대한 정보도 필수로 작성하여야 합니다. <br>*실제 DB에 반영되지 않음*")
    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> createCategory(@RequestBody @Valid final CategorySavePayload categorySavePayload,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }
        if(categorySavePayload.getType().equals(CategoryType.SUB) && categorySavePayload.getParentName() == null){
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 카테고리 수정 API
     * @param id 수정할 카테고리 id
     * @param bindingResult validation
     * @return
     */
    @Operation(summary = "카테고리 수정", description = "특정 카테고리를 수정합니다. <br>id가 1,2,3 인 데이터에 대해서만 수정 요청을 할 수 있도록 하였습니다. <br>*실제 DB에 반영되지 않음*")
    @PutMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> updateCategory(
        @PathVariable(name = "id")
        @Schema(description = "카테고리 id", example = "3")
        final Long id,
        @RequestBody @Valid final CategorySavePayload categorySavePayload,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }
        // 존재하지 않는 카테고리 아이디로 접근
        if(id != 1L && id != 2L && id != 3L){
            return ResponseEntity
                .status(ResponseCode.NOT_EXIST_CATEGORY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_EXIST_CATEGORY));
        }

        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 카테고리 삭제 API
     * @param id 삭제할 카테고리 id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "카테고리 삭제", description = "특정 카테고리를 삭제합니다. <br>id가 1,2,3 인 데이터에 대해서만 삭제 요청을 할 수 있도록 하였습니다. <br>*실제 DB에 반영되지 않음*")
    public ResponseEntity<CommonApiResponse<Void>> deleteCategory(
        @PathVariable(name = "id")
        @Schema(description = "카테고리 id", example = "1") final Long id){
        // 존재하지 않는 카테고리 아이디로 접근
        if(id != 1L && id != 2L && id != 3L){
            return ResponseEntity
                .status(ResponseCode.NOT_EXIST_CATEGORY.status())
                .body(CommonApiResponse.error(ResponseCode.NOT_EXIST_CATEGORY));
        }

        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

}
