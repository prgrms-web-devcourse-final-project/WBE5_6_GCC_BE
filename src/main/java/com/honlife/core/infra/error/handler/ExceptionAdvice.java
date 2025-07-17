package com.honlife.core.infra.error.handler;

import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.http.HttpStatus;


@RestControllerAdvice
public class ExceptionAdvice {

  @ExceptionHandler(CommonException.class)
  public ResponseEntity<ErrorResponse> handleCommonException(CommonException ex) {
    ResponseCode code = ex.code();
    HttpStatus status = code.getStatus();
    return ResponseEntity
        .status(status)
        .body(new ErrorResponse(
            status,
            code.getCode(),
            code.getMessage()
        ));
  }


}
