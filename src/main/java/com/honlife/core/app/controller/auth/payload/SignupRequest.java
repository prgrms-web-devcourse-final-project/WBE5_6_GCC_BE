package com.honlife.core.app.controller.auth.payload;

import com.honlife.core.app.model.member.code.ResidenceExperience;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
public class SignupRequest {

    @Schema(description = "이메일", example = "user03@test.com")
    private String email;
    @Schema(description = "비밀번호", example = "3333")
    private String password;
    @Schema(description = "이름", example = "조철봉")
    private String name;
}
