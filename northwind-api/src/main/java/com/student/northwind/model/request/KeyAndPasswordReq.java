package com.student.northwind.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyAndPasswordReq {
    private String key;

    private String newPassword;
}
