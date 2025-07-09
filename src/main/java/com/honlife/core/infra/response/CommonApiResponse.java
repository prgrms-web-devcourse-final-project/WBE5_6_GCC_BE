package com.honlife.core.infra.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공통 응답 DTO")
public record CommonApiResponse<T>(
    @Schema(description = "서비스 정의 코드", example = "2000")
    String code,
    @Schema(description = "응답 메시지", example = "성공적으로 처리되었습니다.")
    String message,
    @Schema(description = "응답 데이터")
    T data
) {
    public static <T> CommonApiResponse<T> success(T data) {
        return new CommonApiResponse<>(ResponseCode.OK.code(), ResponseCode.OK.message(), data);
    }
    
    public static <T> CommonApiResponse<T> noContent() {
        return new CommonApiResponse<>(ResponseCode.OK.code(), ResponseCode.OK.message(), null);
    }
    
    public static <T> CommonApiResponse<T> error(ResponseCode code) {
        return new CommonApiResponse<>(code.code(), code.message(), null);
    }
    
    public static <T> CommonApiResponse<T> error(ResponseCode code, T data) {
        return new CommonApiResponse<>(code.code(), code.message(), data);
    }
}
