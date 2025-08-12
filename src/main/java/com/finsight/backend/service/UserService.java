package com.finsight.backend.service;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.request.SignupForm;
import com.finsight.backend.domain.vo.user.UserVO;

import java.util.Optional;

public interface UserService {
    UserVO findUser(LoginForm loginForm);
    void registerUser(SignupForm signupForm);

    void checkUserId(String userId);
    void checkEmail(String email);
    UserVO findByUserId(String userId);

    void deleteUser(String userId);

    void updateUserInfo(String userId, String newPassword, String newEmail);
    String encodePassword(String rawPassword);
}
