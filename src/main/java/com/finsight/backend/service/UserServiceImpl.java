package com.finsight.backend.service;

import com.finsight.backend.common.exception.user.CustomDuplicateException;
import com.finsight.backend.common.exception.user.CustomNotFoundUserException;
import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.request.SignupForm;
import com.finsight.backend.domain.enumerate.UserRole;
import com.finsight.backend.repository.mapper.UserMapper;
import com.finsight.backend.domain.vo.user.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @Override
    public UserVO findUser(LoginForm loginForm) {
        UserVO user = userMapper.findUserByUserId(loginForm.getUserId());
        if(user != null && passwordEncoder.matches(loginForm.getPassword(), user.getUserPassword())){
            return user;
        }
        throw new CustomNotFoundUserException();
    }

    @Transactional
    @Override
    public void registerUser(SignupForm signupForm) {
        emailService.isEmailVerified(signupForm.getEmail());

        if (userMapper.existsByUserId(signupForm.getUserId()) ||
                userMapper.existsByEmail(signupForm.getEmail())) {
            log.warn("[UserService] Duplicated User : {}", signupForm.getUserId());
            throw new CustomDuplicateException();
        }

        UserVO user = new UserVO(
                signupForm.getUserId(),
                signupForm.getUsername(),
                passwordEncoder.encode(signupForm.getPassword()),
                signupForm.getBirthday(),
                signupForm.getEmail(),
                LocalDateTime.now(),
                UserRole.INCOMPLETE);

        userMapper.insert(user);
        emailService.removeVerifiedEmail(signupForm.getEmail());  // ✅ 인증된 목록에서 제거
    }

    @Override
    public void checkUserId(String userId) {
        userMapper.existsByUserId(userId);
    }


    @Override
    public void checkEmail(String email) {
        userMapper.existsByEmail(email);
    }

    @Override
    public UserVO findByUserId(String userId) {
        return userMapper.findUserByUserId(userId);
    }
    @Override
    public void deleteUser(String userId) {
        userMapper.deleteUser(userId);
    }

    @Transactional
    @Override
    public void updateUserInfo(String userId, String newPassword, String newEmail) {
        if (newPassword != null && !newPassword.isBlank()) {
            String encodedPw = passwordEncoder.encode(newPassword);
            userMapper.updatePassword(userId, encodedPw);
        }

        if (newEmail != null && !newEmail.isBlank()) {
            emailService.isEmailVerified(newEmail);
            userMapper.updateEmail(userId, newEmail);
            emailService.removeVerifiedEmail(newEmail);
        }
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}