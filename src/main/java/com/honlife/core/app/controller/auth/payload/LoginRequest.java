package com.honlife.core.app.controller.auth.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {

    @Schema(description = "사용자 이메일", example = "test1@test.com")
    private String email;

    @Schema(description = "비밀번호", example = "123qwe!@#")
    private String password;
}
