package com.honlife.core.infra.oauth2.dto;

import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KakaoUserDetails implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        Object kakaoAccountObj = attributes.get("kakao_account");
        if (kakaoAccountObj instanceof Map<?, ?> kakaoAccount) {
            Object emailObj = kakaoAccount.get("email");
            if (emailObj != null) {
                return emailObj.toString();
            }
        }
        return null; // 또는 예외 발생
//        return (String) ((Map<?, ?>) attributes.get("kakao_account")).get("email");
//        return null;
    }

    @Override
    public String getName() {
        return (String) ((Map<?, ?>) attributes.get("properties")).get("nickname");
    }
}
