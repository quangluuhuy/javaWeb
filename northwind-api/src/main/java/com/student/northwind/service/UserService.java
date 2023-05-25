package com.student.northwind.service;

import com.student.northwind.domain.Authority;
import com.student.northwind.domain.User;
import com.student.northwind.error.ErrorCode;
import com.student.northwind.error.ErrorCodeException;
import com.student.northwind.mapper.UserMapper;
import com.student.northwind.model.request.AdminUserReq;
import com.student.northwind.model.response.UserLiteResp;
import com.student.northwind.model.response.UserResp;
import com.student.northwind.repository.AuthorityRepository;
import com.student.northwind.repository.UserRepository;
import com.student.northwind.security.SecurityUtils;
import com.student.northwind.util.AuthoritiesConstants;
import com.student.northwind.util.Constants;
import com.student.northwind.util.RandomUtil;
import com.student.northwind.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final CacheManager cacheManager;
    private final UserValidator userValidator;
    private final UserMapper userMapper;


    public Optional<UserResp> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
                .findOneByActivationKey(key)
                .map(userMapper::userToUserResp);
    }

    public Optional<UserResp> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        userValidator.passwordValid(newPassword);
        return userRepository
                .findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
                .map(user -> {
                    this.clearUserCaches(user);
                    return userMapper.userToUserResp(user);
                });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
                .findOneByEmailIgnoreCase(mail)
                .filter(User::isActivated)
                .map(user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    this.clearUserCaches(user);
                    return user;
                });
    }

    public UserResp registerUser(AdminUserReq adminUserReq, String password) {
        userValidator.passwordValid(password);
        userRepository
                .findOneByLogin(adminUserReq.getLogin().toLowerCase())
                .ifPresent(existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new ErrorCodeException(ErrorCode.NWD_103_USERNAME_ALREADY_USED);
                    }
                });
        userRepository
                .findOneByEmailIgnoreCase(adminUserReq.getEmail())
                .ifPresent(existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new ErrorCodeException(ErrorCode.NWD_107_EMAIL_ALREADY_USED);
                    }
                });

        var newUser = userMapper.adminUserReqToUser(adminUserReq); //new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encryptedPassword);
        //To test: new user is activate. In prod: new user not activate, link activate will send to user's email
        newUser.setActivated(true);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return userMapper.userToUserResp(newUser);
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(AdminUserReq adminUserReq) {
        var user = userMapper.adminUserReqToUser(adminUserReq);

        if (adminUserReq.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        }

        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param adminUserReq user to update.
     * @return updated user.
     */
    public Optional<AdminUserReq> updateUser(AdminUserReq adminUserReq) {
        return Optional
                .of(userRepository.findById(adminUserReq.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    this.clearUserCaches(user);
                    user.setLogin(adminUserReq.getLogin().toLowerCase());
                    user.setFirstName(adminUserReq.getFirstName());
                    user.setLastName(adminUserReq.getLastName());
                    if (adminUserReq.getEmail() != null) {
                        user.setEmail(adminUserReq.getEmail().toLowerCase());
                    }
                    user.setImageUrl(adminUserReq.getImageUrl());
                    user.setActivated(adminUserReq.isActivated());
                    user.setLangKey(adminUserReq.getLangKey());
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    managedAuthorities.clear();
                    adminUserReq
                            .getAuthorities()
                            .stream()
                            .map(authorityRepository::findById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .forEach(managedAuthorities::add);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                })
                .map(AdminUserReq::new);
    }

    public void deleteUser(String login) {
        userRepository
                .findOneByLogin(login)
                .ifPresent(user -> {
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                    log.debug("Deleted User: {}", user);
                });
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils
                .getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin)
                .ifPresent(user -> {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    if (email != null) {
                        user.setEmail(email.toLowerCase());
                    }
                    user.setLangKey(langKey);
                    user.setImageUrl(imageUrl);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        userValidator.passwordValid(newPassword);
        SecurityUtils
                .getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin)
                .ifPresent(user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new ErrorCodeException(ErrorCode.NWD_102_INVALID_PASSWORD);
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    this.clearUserCaches(user);
                    log.debug("Changed password for User: {}", user);
                });
    }

    @Transactional(readOnly = true)
    public Page<AdminUserReq> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserReq::new);
    }

    @Transactional(readOnly = true)
    public Page<UserLiteResp> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserLiteResp::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
                .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
                .forEach(user -> {
                    log.debug("Deleting not activated user {}", user.getLogin());
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public void updateAccount(AdminUserReq adminUserReq) {
        userValidator.updateAccountReqValid(adminUserReq);
        updateUser(
                adminUserReq.getFirstName(),
                adminUserReq.getLastName(),
                adminUserReq.getEmail(),
                adminUserReq.getLangKey(),
                adminUserReq.getImageUrl()
        );
    }
}
