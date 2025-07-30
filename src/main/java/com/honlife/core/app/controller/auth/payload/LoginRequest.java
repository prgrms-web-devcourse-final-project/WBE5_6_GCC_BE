package com.honlife.core.app.controller.auth.payload;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;

    private String password;
}
