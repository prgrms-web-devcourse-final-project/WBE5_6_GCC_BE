package com.honlife.core.infra.response;

import org.springframework.http.HttpStatus;

public enum ResponseCode {
    OK("0000", HttpStatus.OK, "OK"),
    BAD_REQUEST("4000", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_FILENAME("4001", HttpStatus.BAD_REQUEST, "사용 할 수 없는 파일 이름입니다."),
    UNAUTHORIZED("4010", HttpStatus.UNAUTHORIZED, "Authentication required"),
    BAD_CREDENTIAL("4011", HttpStatus.UNAUTHORIZED, "아이디나 비밀번호가 틀렸습니다."),
    INVALID_CODE("4012", HttpStatus.UNAUTHORIZED, "Invalid verification code"),
    NOT_EXIST_PRE_AUTH_CREDENTIAL("4013", HttpStatus.OK, "사전 인증 정보가 요청에서 발견되지 않았습니다."),
    NOT_EXIST_MEMBER("4040", HttpStatus.NOT_FOUND, "Member not exist."),
    NOT_EXIST_BADGE("4041", HttpStatus.NOT_FOUND, "badge not exist."),
    ALREADY_CLAIMED_BADGE("4090", HttpStatus.CONFLICT, "You have already claimed this badge."),
    INTERNAL_SERVER_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    SECURITY_INCIDENT("6000", HttpStatus.OK, "비정상적인 로그인 시도가 감지되었습니다."),
    NOT_EXIST_EVENT_QUEST("4704", HttpStatus.NOT_FOUND,"해당 이벤트 퀘스트가 존재하지 않습니다."),
    NOT_EXIST_ITEM("4804", HttpStatus.NOT_FOUND,"해당 상점 아이템이 존재하지 않습니다."),
    NOT_EXIST_PRESET("4604",HttpStatus.NOT_FOUND, "존재하지 않는 추천 루틴입니다."),
    NOT_EXIST_QUEST("4605", HttpStatus.NOT_FOUND, "존재하지 않는 퀘스트입니다."),
    NOT_EXIST_LOG("4606",HttpStatus.NOT_FOUND ,"존재하지 않는 로그입니다.");




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
