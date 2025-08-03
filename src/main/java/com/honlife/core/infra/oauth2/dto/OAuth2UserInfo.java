package com.honlife.core.infra.oauth2.dto;

import java.util.Map;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();

    static OAuth2UserInfo createUserInfo(String path, OAuth2User user) {
        Map<String, OAuth2UserInfo> map = Map.of(
            "/login/oauth2/code/kakao", new KakaoUserDetails(user.getAttributes()),
            "/login/oauth2/code/google", new GoogleUserDetails(user.getAttributes())
        );

        return map.get(path);
    }
}
