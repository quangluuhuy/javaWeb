package com.student.northwind.error;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {
    private String errorCode;
    private String message;
    private List<ErrorField> fields = new ArrayList<>();;

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorResponse(String errorCode, String message, List<ErrorField> fields) {
        this.errorCode = errorCode;
        this.message = message;
        this.fields = fields;
    }

    public void addFieldError(ErrorField errorField) {
        fields.add(errorField);
    }
}
