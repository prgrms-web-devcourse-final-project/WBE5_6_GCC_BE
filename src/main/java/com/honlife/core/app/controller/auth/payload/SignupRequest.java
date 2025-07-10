package com.honlife.core.app.controller.auth.payload;

import com.honlife.core.app.model.member.code.ResidenceExperience;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignupRequest {

    @Schema(description = "이메일", example = "user03@test.com")
    private String email;
    @Schema(description = "비밀번호", example = "3333")
    private String password;
    @Schema(description = "이름", example = "조철봉")
    private String name;
    @Schema(description = "닉네임", example = "닉네임3")
    private String nickname;
    @Schema(description = "거주 경력", example = "UNDER_1Y", allowableValues = {"UNDER_1Y", "Y1_TO_3", "Y3_TO_5", "Y5_TO_10", "OVER_10Y"})
    private ResidenceExperience residenceExperience;
    @Schema(description = "시/도", example = "서울특별시")
    private String region1Dept;
    @Schema(description = "시/군/구", example = "성북구")
    private String region2Dept;
    @Schema(description = "동/읍/면", example = "정릉동")
    private String region3Dept;
}
