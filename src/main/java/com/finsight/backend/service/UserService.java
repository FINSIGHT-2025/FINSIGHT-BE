package com.finsight.backend.service;

import com.finsight.backend.enumerate.Role;
import com.finsight.backend.mapper.UserMapper;
import com.finsight.backend.vo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/*
비즈니스 로직을 처리하는 서비스 클래스
중복 확인 로직 (isUserIdTaken, isNicknameTaken, isEmailTaken)
회원가입 처리 (registerMember)
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;  // ✅ 이메일 인증 여부 확인을 위한 의존성 주입

    public boolean isUserIdTaken(String userId) {
        return userMapper.countByUserId(userId) > 0;
    }

    public boolean isNicknameTaken(String nickname) {
        return userMapper.countByNickname(nickname) > 0;
    }

    public boolean isEmailTaken(String email) {
        return userMapper.countByEmail(email) > 0;
    }

    public boolean registerMember(Member member) {
        // ✅ 이메일 인증 여부 체크
        if (!emailService.isEmailVerified(member.getEmail())) {
            return false;
        }

        if (isUserIdTaken(member.getUserId()) ||
                isNicknameTaken(member.getNickname()) ||
                isEmailTaken(member.getEmail())) {
            return false;
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setCreatedAt(LocalDateTime.now());
        member.setRole(Role.INCOMPLETE); // 기본 권한

        boolean result = userMapper.insertMember(member) == 1;

        if (result) {
            emailService.removeVerifiedEmail(member.getEmail());  // ✅ 인증된 목록에서 제거
        }

        return result;
    }
}

//@Service
//public class UserService {
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public boolean isUserIdTaken(String userId) {
//        return userMapper.countByUserId(userId) > 0;
//    }
//
//    public boolean isNicknameTaken(String nickname) {
//        return userMapper.countByNickname(nickname) > 0;
//    }
//
//    public boolean isEmailTaken(String email) {
//        return userMapper.countByEmail(email) > 0;
//    }
//
//    public boolean registerMember(Member member) {
//        if (isUserIdTaken(member.getUserId()) ||
//                isNicknameTaken(member.getNickname()) ||
//                isEmailTaken(member.getEmail())) {
//            return false;
//        }
//
//        member.setPassword(passwordEncoder.encode(member.getPassword()));
//        member.setCreatedAt(LocalDateTime.now());
//        member.setRole(Role.INCOMPLETE); // default
//
//        return userMapper.insertMember(member) == 1;
//    }
//}

