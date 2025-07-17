package com.honlife.core.app.controller.auth.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginRequest {

    @Schema(description = "사용자 이메일", example = "user01@test.com")
    private String email = "user01@test.com";

    @Schema(description = "비밀번호", example = "1111")
    private String password = "1111";
}
