package com.student.northwind.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class ManagedUserReq extends AdminUserReq {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    //do not write password toString
    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
