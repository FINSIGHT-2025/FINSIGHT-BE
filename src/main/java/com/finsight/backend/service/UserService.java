package com.finsight.backend.service;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.request.SignupForm;
import com.finsight.backend.vo.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findUser(LoginForm loginForm);
    boolean registerUser(SignupForm signupForm);

    boolean checkUserId(String userId);
    boolean checkEmail(String email);

    Optional<User> findByUserId(String userId);

    void deleteUser(String userId);

    void updateUserInfo(String userId, String newPassword, String newEmail);
    String encodePassword(String rawPassword);
}
