package com.honlife.core.app.controller.auth.payload;

import lombok.Data;

@Data
public class DuplicationCheckRequest {

    private String email;
    private String nickname;
}
