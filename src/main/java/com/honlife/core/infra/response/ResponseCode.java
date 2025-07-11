package com.honlife.core.infra.response;

import org.springframework.http.HttpStatus;

public enum ResponseCode {
    OK("0000", HttpStatus.OK, "OK"),
    BAD_REQUEST("4000", HttpStatus.BAD_REQUEST, "Bad Request."),
    INVALID_FILENAME("4001", HttpStatus.BAD_REQUEST, "Unsupported Filename."),
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
    GRANT_CONFLICT_BADGE("4090", HttpStatus.CONFLICT, "Badge Already granted."),
    GRANT_CONFLICT_POINT("4091", HttpStatus.CONFLICT, "Point Already granted."),
    GRANT_CONFLICT_ITEM("4092", HttpStatus.CONFLICT, "Item Already granted."),
    ASSIGN_CONFLICT_QUEST("4093", HttpStatus.CONFLICT, "Quest Already assigned."),
    INTERNAL_SERVER_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    SECURITY_INCIDENT("6000", HttpStatus.OK, "비정상적인 로그인 시도가 감지되었습니다."),
    NOT_EXIST_EVENT_QUEST("4704", HttpStatus.NOT_FOUND,"해당 이벤트 퀘스트가 존재하지 않습니다."),
    NOT_EXIST_ITEM("4804", HttpStatus.NOT_FOUND,"해당 상점 아이템이 존재하지 않습니다."),
    NOT_EXIST_PRESET("4604",HttpStatus.NOT_FOUND, "존재하지 않는 추천 루틴입니다."),
    NOT_EXIST_QUEST("4605", HttpStatus.NOT_FOUND, "존재하지 않는 퀘스트입니다."),
    NOT_EXIST_LOG("4606",HttpStatus.NOT_FOUND ,"존재하지 않는 로그입니다.");
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
