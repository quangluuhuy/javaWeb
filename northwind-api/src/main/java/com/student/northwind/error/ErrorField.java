package com.student.northwind.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorField {
    private final String objectName;
    private final String fieldName;
    private final String message;
}
