package com.student.northwind.error;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorCodeException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String message;
    private List<String> params;
    private List<ErrorField> fields;

    public ErrorCodeException(ErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
        this.message = errorCode.message();
    }

    public ErrorCodeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCodeException(ErrorCode errorCode, List<String> params) {
        super(errorCode.message());
        this.errorCode = errorCode;
        this.message = errorCode.message();
        this.params = params;
    }

    public ErrorCodeException(ErrorCode errorCode, String message, List<String> params) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.params = params;
    }
}
