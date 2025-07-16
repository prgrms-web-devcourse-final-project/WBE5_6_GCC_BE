package com.honlife.core.app.controller.member.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberUpdatePasswordRequest {

    @NotBlank
    private String newPassword;
}
