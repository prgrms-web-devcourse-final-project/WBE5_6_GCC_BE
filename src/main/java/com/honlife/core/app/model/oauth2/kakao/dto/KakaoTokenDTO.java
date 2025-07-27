package com.honlife.core.app.model.oauth2.kakao.dto;

import lombok.Getter;

@Getter
public class KakaoTokenDTO {

    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;
    private int refresh_token_expires_in;
    private String scope;

}
