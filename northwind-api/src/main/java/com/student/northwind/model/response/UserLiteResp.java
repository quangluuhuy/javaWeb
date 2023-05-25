package com.student.northwind.model.response;

import com.student.northwind.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLiteResp {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String login;

    public UserLiteResp(User user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.login = user.getLogin();
    }
}
