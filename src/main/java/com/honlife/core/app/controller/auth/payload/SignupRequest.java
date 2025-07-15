package com.honlife.core.app.controller.auth.payload;

import com.honlife.core.app.model.member.code.ResidenceExperience;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String nickname;
    private List<Long> interestedCategoryIds;
    private ResidenceExperience residenceExperience;
    private String region1Dept;
    private String region2Dept;
    private String region3Dept;
}
