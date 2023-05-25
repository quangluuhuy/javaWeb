package com.student.northwind.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private String currentPassword;
    private String newPassword;
}
