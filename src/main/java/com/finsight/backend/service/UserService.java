package com.finsight.backend.service;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.request.SignupForm;
import com.finsight.backend.domain.vo.user.UserVO;

import java.util.Optional;

public interface UserService {
    Optional<UserVO> findUser(LoginForm loginForm);
    boolean registerUser(SignupForm signupForm);

    boolean checkUserId(String userId);
    boolean checkEmail(String email);

    Optional<UserVO> findByUserId(String userId);

    boolean deleteUser(String userId);

    boolean updateUserInfo(String userId, String newPassword, String newEmail);
    String encodePassword(String rawPassword);
}
