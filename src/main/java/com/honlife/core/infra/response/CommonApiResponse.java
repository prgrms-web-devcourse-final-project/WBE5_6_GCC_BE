package com.honlife.core.infra.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommonApiResponse<T>(
    String code,
    String message,
    T data
) {
    public static <T> CommonApiResponse<T> success(T data) {
        return new CommonApiResponse<>(ResponseCode.OK.code(), ResponseCode.OK.message(), data);
    }

    public static <T> CommonApiResponse<T> success(ResponseCode code) {
        return new CommonApiResponse<>(code.code(), code.message(), null);
    }

    public static <T> CommonApiResponse<T> noContent() {
        return new CommonApiResponse<>(ResponseCode.OK.code(), ResponseCode.OK.message(), null);
    }

    public static <T> CommonApiResponse<T> noContent(ResponseCode code) {
        return new CommonApiResponse<>(code.code(), code.message(), null);
    }
    
    public static <T> CommonApiResponse<T> error(ResponseCode code) {
        return new CommonApiResponse<>(code.code(), code.message(), null);
    }
    
    public static <T> CommonApiResponse<T> error(ResponseCode code, T data) {
        return new CommonApiResponse<>(code.code(), code.message(), data);
    }
}
