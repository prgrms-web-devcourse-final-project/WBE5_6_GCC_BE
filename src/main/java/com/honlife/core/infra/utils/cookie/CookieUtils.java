package com.honlife.core.infra.utils.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, String domain) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .maxAge(maxAge)
            .domain(domain) // 도메인 속성 추가
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name, String domain) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
            .path("/")
            .maxAge(0)
            .secure(true)
            .sameSite("None")
            .httpOnly(true)
            .domain(domain) // 도메인 속성 추가
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
            Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
