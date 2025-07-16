package com.honlife.core.app.controller.auth.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 회원가입 Phase 1 에서 사용됩니다.
 */
@Getter
public class SignupBasicRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
}
