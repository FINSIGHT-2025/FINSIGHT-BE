package com.finsight.backend.service;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.request.SignupForm;
import com.finsight.backend.enumerate.UserRole;
import com.finsight.backend.mapper.UserMapper;
import com.finsight.backend.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper UserMapper;
    private final EmailService emailService;

    @Override
    public Optional<User> findUser(LoginForm loginForm) {
        User user = UserMapper.findUserByUserId(loginForm.getUserId());
        if(user != null && passwordEncoder.matches(loginForm.getPassword(), user.getUserPassword())){
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public boolean registerUser(SignupForm signupForm) {
        if (!emailService.isEmailVerified(signupForm.getEmail())) {
            return false;
        }

        if (UserMapper.existsByUserId(signupForm.getUserId()) ||
                UserMapper.existsByEmail(signupForm.getEmail())) {
            return false;
        }

        User user = User.builder()
                .userId(signupForm.getUserId())
                .userName(signupForm.getUsername())
                .userPassword(passwordEncoder.encode(signupForm.getPassword()))
                .userBirthday(signupForm.getBirthday())
                .userEmail(signupForm.getEmail())
                .userCreatedAt(LocalDateTime.now())
                .userRole(UserRole.INCOMPLETE)
                .build();

        boolean result = UserMapper.insert(user);

        if (result) {
            emailService.removeVerifiedEmail(signupForm.getEmail());  // ✅ 인증된 목록에서 제거
        }

        return result;
    }

    @Override
    public boolean checkUserId(String userId) {
        return UserMapper.existsByUserId(userId);
    }


    @Override
    public boolean checkEmail(String email) {
        return UserMapper.existsByEmail(email);
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return Optional.ofNullable(UserMapper.findUserByUserId(userId));
    }
    @Override
    public void deleteUser(String userId) {
        UserMapper.deleteUser(userId);
    }

    @Override
    public void updateUserInfo(String userId, String newPassword, String newEmail) {
        if (newPassword != null && !newPassword.isBlank()) {
            String encodedPw = passwordEncoder.encode(newPassword);
            UserMapper.updatePassword(userId, encodedPw);
        }

        if (newEmail != null && !newEmail.isBlank()) {
            if (!emailService.isEmailVerified(newEmail)) {
                throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
            }
            UserMapper.updateEmail(userId, newEmail);
            emailService.removeVerifiedEmail(newEmail);
        }
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}