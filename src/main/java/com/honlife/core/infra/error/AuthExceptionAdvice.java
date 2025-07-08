package com.honlife.core.infra.error;

import com.honlife.core.infra.error.exceptions.AuthApiException;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class AuthExceptionAdvice {
    
    @ResponseBody
    @ExceptionHandler(AuthApiException.class)
    public ResponseEntity<CommonApiResponse<String>> authApiExHandler(
        AuthApiException ex) {
        return ResponseEntity
                   .status(ex.code().status())
                   .body(CommonApiResponse.error(ex.code()));
    }
    
    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CommonApiResponse<String>> authExHandler(
        AuthenticationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                   .status(HttpStatus.UNAUTHORIZED)
                   .body(CommonApiResponse.error(ResponseCode.UNAUTHORIZED));
    }
}
