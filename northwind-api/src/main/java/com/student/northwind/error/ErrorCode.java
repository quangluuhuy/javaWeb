package com.student.northwind.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    NWD_001_INTERNAL_SERVER_ERROR("NWD_001", HttpStatus.INTERNAL_SERVER_ERROR),
    NWD_003_INVALID_FORMAT("NWD_003", HttpStatus.BAD_REQUEST),
    NWD_004_INVALID_REQUEST("NWD_003", HttpStatus.BAD_REQUEST),
    NWD_006_METHOD_ARGS_NOT_VALID("NWD_006", HttpStatus.BAD_REQUEST),
    NWD_008_NULL_POINTER("NWD_008", HttpStatus.INTERNAL_SERVER_ERROR),
    NWD_101_LOGIN_FAIL("NWD_101", HttpStatus.UNAUTHORIZED),
    NWD_102_INVALID_PASSWORD("NWD_102", HttpStatus.BAD_REQUEST),
    NWD_103_USERNAME_ALREADY_USED("NWD_103", HttpStatus.BAD_REQUEST),
    NWD_104_USER_NOT_FOUND_BY_ACTIVATE_KEY("NWD_104", HttpStatus.BAD_REQUEST),
    NWD_105_USER_NOT_FOUND("NWD_105", HttpStatus.BAD_REQUEST),
    NWD_106_USER_NOT_FOUND_BY_RESET_KEY("NWD_106", HttpStatus.BAD_REQUEST),
    NWD_107_EMAIL_ALREADY_USED("NWD_107", HttpStatus.BAD_REQUEST),
    NWD_108_USER_NOT_ACTIVATED("NWD_108", HttpStatus.BAD_REQUEST),
    ;

    private String code;
    private String message;
    private HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    ErrorCode(String code, HttpStatus httpStatus) {
        this.code = code;
        this.message = this.toString();
        this.httpStatus = httpStatus;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
