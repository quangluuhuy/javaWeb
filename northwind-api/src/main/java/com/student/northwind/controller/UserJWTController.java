package com.student.northwind.controller;


import com.student.northwind.config.jwt.JWTFilter;
import com.student.northwind.config.jwt.TokenProvider;
import com.student.northwind.error.ErrorCode;
import com.student.northwind.error.ErrorCodeException;
import com.student.northwind.model.request.*;
import com.student.northwind.model.response.JWTTokenResp;
import com.student.northwind.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Controller to authenticate users.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserJWTController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<JWTTokenResp> authorize(@Valid @RequestBody LoginReq loginReq) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                loginReq.getUsername(),
                loginReq.getPassword()
        );
        var authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwt = tokenProvider.createToken(authentication, loginReq.isRememberMe());

        var httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTTokenResp(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserReq the managed user View Model.
     * @throws ErrorCodeException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws ErrorCodeException {@code 400 (Bad Request)} if the email is already used.
     * @throws ErrorCodeException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserReq managedUserReq) {
        var user = userService.registerUser(managedUserReq, managedUserReq.getPassword());
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        var user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new ErrorCodeException(ErrorCode.NWD_104_USER_NOT_FOUND_BY_ACTIVATE_KEY);
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public AdminUserReq getAccount() {
        return userService
                .getUserWithAuthorities()
                .map(AdminUserReq::new)
                .orElseThrow(() -> new ErrorCodeException(ErrorCode.NWD_105_USER_NOT_FOUND));

    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param adminUserReq the current user information.
     * @throws ErrorCodeException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException   {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserReq adminUserReq) {
        userService.updateAccount(adminUserReq);

    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeReq current and new password.
     * @throws ErrorCodeException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeReq passwordChangeReq) {

        userService.changePassword(passwordChangeReq.getCurrentPassword(), passwordChangeReq.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        var user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            log.info("reset password success");
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws ErrorCodeException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException   {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordReq keyAndPassword) {
        var user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new ErrorCodeException(ErrorCode.NWD_106_USER_NOT_FOUND_BY_RESET_KEY);
        }
    }

}