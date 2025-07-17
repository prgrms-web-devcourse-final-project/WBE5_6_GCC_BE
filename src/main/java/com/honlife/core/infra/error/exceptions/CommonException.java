package com.honlife.core.infra.error.exceptions;

import com.honlife.core.infra.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class CommonException extends RuntimeException {
    
    private final ResponseCode code;
    
    public CommonException(ResponseCode code) {
        this.code = code;
    }
    
    public CommonException(ResponseCode code, Exception e) {
        this.code = code;
        log.error(e.getMessage(), e);
    }
    
    public ResponseCode code() {
        return code;
    }

    public String messageFromCode() {
        return code != null ? code.getMessage() : null;
    }

    public String statusCode() {
        return code != null ? code.getCode() : null;
    }

    public HttpStatus httpStatus() {
        return code != null ? code.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
    }


}
