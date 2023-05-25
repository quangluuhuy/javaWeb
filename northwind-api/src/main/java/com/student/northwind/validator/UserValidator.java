package com.student.northwind.validator;

import com.student.northwind.error.ErrorCode;
import com.student.northwind.error.ErrorCodeException;
import com.student.northwind.model.request.AdminUserReq;
import com.student.northwind.model.request.ManagedUserReq;
import com.student.northwind.repository.UserRepository;
import com.student.northwind.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;
    public void passwordValid(String password) {
        if (StringUtils.isEmpty(password) ||
                password.length() < ManagedUserReq.PASSWORD_MIN_LENGTH ||
                password.length() > ManagedUserReq.PASSWORD_MAX_LENGTH) {
            throw new ErrorCodeException(ErrorCode.NWD_102_INVALID_PASSWORD);
        }
    }

    public void updateAccountReqValid(AdminUserReq adminUserReq) {
        var userLogin = SecurityUtils
                .getCurrentUserLogin()
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.NWD_105_USER_NOT_FOUND, "CURRENT_USER_NOT_FOUND"));
        var existingUser = userRepository.findOneByEmailIgnoreCase(adminUserReq.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new ErrorCodeException(ErrorCode.NWD_107_EMAIL_ALREADY_USED);
        }
        var user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new ErrorCodeException(ErrorCode.NWD_105_USER_NOT_FOUND, "USER_COULD_NOT_BE_FOUND");
        }
    }
}
