package com.honlife.core.app.controller.auth.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyEmailRequest {

    @NotBlank
    @Schema(description="사용자 이메일", example="user01@test.com")
    private String email;
    @Schema(description="이메일 인증 코드", example="null")
    private String code;
}
