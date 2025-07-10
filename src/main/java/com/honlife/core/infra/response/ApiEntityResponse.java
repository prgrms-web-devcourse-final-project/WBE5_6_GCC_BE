package com.honlife.core.infra.response;

public class ApiEntityResponse {
  private int status;
  private String message;
   private Object data;

  public ApiEntityResponse(int status, String message) {
    this.status = status;
    this.message = message;
  }

  public ApiEntityResponse(int status, String message, Object data) {
    this.status = status;
    this.message = message;
    this.data = data;
  }
  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
