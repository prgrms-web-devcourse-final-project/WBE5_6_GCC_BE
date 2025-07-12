package com.honlife.core.app.controller.category;

import com.honlife.core.app.controller.category.payload.InterestCategoryResponse;
import com.honlife.core.app.controller.category.payload.UpdateInterestCategoryRequest;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.category.service.InterestCategoryService;

@SecurityRequirement(name = "bearerAuth")
@Tag(name="선호 카테고리", description = "선호 카테고리 관련 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/categories/interests", produces = MediaType.APPLICATION_JSON_VALUE)
public class InterestCategoryController {

    private final InterestCategoryService interestCategoryService;

    public InterestCategoryController(final InterestCategoryService interestCategoryService) {
        this.interestCategoryService = interestCategoryService;
    }

    /**
     * 선호 카테고리 조회
     * @param userDetails
     * @return
     */
    @Operation(summary = "선호 카테고리 조회", description = "로그인한 회원의 선호 카테고리를 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<InterestCategoryResponse>>> getInterestCategories(@AuthenticationPrincipal UserDetails userDetails) {

        if(userDetails.getUsername().equals("user01@test.com")) {
            List<InterestCategoryResponse> response = new ArrayList<>();

            response.add(InterestCategoryResponse.builder()
                .categoryId(1L)
                .categoryName("청소").build());
            response.add(InterestCategoryResponse.builder()
                .categoryId(2L)
                .categoryName("요리").build());

            return ResponseEntity.ok(CommonApiResponse.success(response));

        }
        return ResponseEntity.status(ResponseCode.UNAUTHORIZED.status())
            .body(CommonApiResponse.error(ResponseCode.UNAUTHORIZED));

    }

    /**
     * 로그인한 회원의 관심 카테고리 정보를 수정하는 API입니다.
     * 요청 본문으로 새로운 관심 카테고리 정보를 전달받아 해당 사용자의 선호 카테고리 목록을 갱신합니다.
     * @param userDetails 로그인한 회원 정보 (Spring Security에서 주입)
     * @param updateInterestCategoryRequest 수정할 관심 카테고리 정보
     * @param bindingResult 요청 본문에 대한 유효성 검사 결과
     * @return 유효하지 않은 요청일 경우 {@code 400 Bad Request}, 사용자가 존재하지 않을 경우 {@code 404 Not Found}를 반환합니다.
     */
    @PutMapping
    public ResponseEntity<CommonApiResponse<Void>> updateInterestCategory(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody @Valid final UpdateInterestCategoryRequest updateInterestCategoryRequest,
        BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.status())
                .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
        }

        if(userDetails.getUsername().equals("user01@test.com")) {
            return ResponseEntity.ok(CommonApiResponse.noContent());
        }
        return ResponseEntity.status(ResponseCode.UNAUTHORIZED.status())
            .body(CommonApiResponse.error(ResponseCode.UNAUTHORIZED));

    }
}
