package com.honlife.core.infra.response;

import org.springframework.http.HttpStatus;

/**
 * 1 - Member<br>
 * 2 - Badge<br>
 * 3 - Routine<br>
 * 4 - Item<br>
 * 5 - Category<br>
 * 6 - Quest<br>
 * 7 - Policy<br>
 * 8 - Point
 */
public enum ResponseCode {
  OK("0000", HttpStatus.OK, "OK"),

  CREATED("2010", HttpStatus.CREATED, "Created"),
  BADGE_CREATED("2012", HttpStatus.CREATED, "Badge created"),
  ROUTINE_CREATED("2013", HttpStatus.CREATED, "Routine created"),
  ITEM_CREATED("2014", HttpStatus.CREATED, "Item created"),
  CATEGORY_CREATED("2015", HttpStatus.CREATED, "Category created"),
  QUEST_CREATED("2016", HttpStatus.CREATED, "Quest created"),

  BAD_REQUEST("4000", HttpStatus.BAD_REQUEST, "Bad Request."),

  UNAUTHORIZED("4010", HttpStatus.UNAUTHORIZED, "Authentication required"),
  BAD_CREDENTIAL("4011", HttpStatus.UNAUTHORIZED, "Wrong credentials."),
  INVALID_CODE("4012", HttpStatus.UNAUTHORIZED, "Invalid verification code"),
  NOT_EXIST_PRE_AUTH_CREDENTIAL("4013", HttpStatus.OK, "No authentication credentials were found in the request."),

  NOT_FOUND("4040", HttpStatus.NOT_FOUND, "Not found."),
  NOT_FOUND_MEMBER("4041", HttpStatus.NOT_FOUND, "Member not found."),
  NOT_FOUND_BADGE("4042", HttpStatus.NOT_FOUND, "Badge not found."),
  NOT_FOUND_ROUTINE("4043", HttpStatus.NOT_FOUND, "Routine not found."),
  NOT_FOUND_ITEM("4044", HttpStatus.NOT_FOUND, "Item not found."),
  NOT_FOUND_CATEGORY("4045", HttpStatus.NOT_FOUND, "Category not found."),
  NOT_FOUND_QUEST("4046", HttpStatus.NOT_FOUND, "Quest not found."),
  NOT_FOUND_POLICY("4047", HttpStatus.NOT_FOUND, "Policy not found."),

  CONFLICT_EXIST_MEMBER("4091", HttpStatus.CONFLICT, "Member already exists."),
  GRANT_CONFLICT_BADGE("4092", HttpStatus.CONFLICT, "Badge Already granted."),
  GRANT_CONFLICT_POINT("4098", HttpStatus.CONFLICT, "Point Already granted."),
  GRANT_CONFLICT_ITEM("4094", HttpStatus.CONFLICT, "Item Already granted."),
  ASSIGN_CONFLICT_QUEST("4096", HttpStatus.CONFLICT, "Quest Already assigned."),

  INTERNAL_SERVER_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
  SECURITY_INCIDENT("6000", HttpStatus.OK, "An unusual login attempt has been detected.");



  private final String code;
  private final HttpStatus status;
  private final String message;

  ResponseCode(String code, HttpStatus status, String message) {
    this.code = code;
    this.status = status;
    this.message = message;
  }

  public String code() {
    return code;
  }

  public HttpStatus status() {
    return status;
  }

  public String message() {
    return message;
  }
}