package com.honlife.core.infra.auth.jwt;

import org.springframework.http.ResponseCookie;

public class TokenCookieFactory {

    public static ResponseCookie create(String name, String value, Long expires, String domain) {
        return ResponseCookie.from(name, value)
            .domain(domain)
            .maxAge(expires)
            .path("/")
            .httpOnly(true)             // HttpOnly
            .secure(true)
            .sameSite("None")// Secure
            .build();
    }

    public static ResponseCookie createExpiredToken(String name, String domain) {
        return ResponseCookie.from(name, "")
            .domain(domain)
            .maxAge(0)
            .path("/")
            .httpOnly(true)             // HttpOnly
            .secure(true)
            .sameSite("None")// // Secure
            .build();
    }
}
