package com.honlife.core.app.controller.auth.payload;

import com.honlife.core.app.model.member.code.ResidenceExperience;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;

/**
 * 회원가입 Phase 2 에서 사용됩니다.
 */
@Getter
public class SignupAdditionalRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String nickname;
    private List<Long> interestedCategoryIds;
    private ResidenceExperience residenceExperience;
    private String region1Dept;
    private String region2Dept;
    private String region3Dept;
}
