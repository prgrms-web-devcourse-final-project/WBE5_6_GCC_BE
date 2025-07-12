package com.honlife.core.app.controller.category;

import com.honlife.core.app.controller.category.payload.InterestCategoryPayload;
import com.honlife.core.app.controller.category.payload.UpdateInterestCategoryRequest;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import com.honlife.core.app.model.category.dto.InterestCategoryDTO;
import com.honlife.core.app.model.category.service.InterestCategoryService;

@SecurityRequirement(name = "bearerAuth")
@Tag(name="선호 카테고리", description = "선호 카테고리 관련 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/interestCategories", produces = MediaType.APPLICATION_JSON_VALUE)
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
    public ResponseEntity<CommonApiResponse<List<InterestCategoryPayload>>> getInterestCategories(@AuthenticationPrincipal UserDetails userDetails) {

        if(userDetails.getUsername().equals("user01@test.com")) {
            List<InterestCategoryPayload> response = new ArrayList<>();

            response.add(InterestCategoryPayload.builder()
                .categoryId(1L)
                .categoryName("청소").build());
            response.add(InterestCategoryPayload.builder()
                .categoryId(2L)
                .categoryName("요리").build());

            return ResponseEntity.ok(CommonApiResponse.success(response));

        }
        return ResponseEntity.status(ResponseCode.UNAUTHORIZED.status())
            .body(CommonApiResponse.error(ResponseCode.UNAUTHORIZED));

    }

}
