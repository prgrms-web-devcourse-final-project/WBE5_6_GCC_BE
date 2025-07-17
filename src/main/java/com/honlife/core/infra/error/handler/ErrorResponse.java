package com.honlife.core.infra.error.handler;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
  private final int status;
  private final String code;
  private final String message;

  public ErrorResponse(HttpStatus status, String code, String message) {
    this.status = status.value();
    this.code = code;
    this.message = message;
  }

  public int getStatus() {
    return status;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}

