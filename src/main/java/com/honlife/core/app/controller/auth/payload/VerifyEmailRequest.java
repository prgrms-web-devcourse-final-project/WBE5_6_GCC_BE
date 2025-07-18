package com.honlife.core.app.controller.auth.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifyEmailRequest {

    @NotBlank
    private String email;
    private String code;
}
