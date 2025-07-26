package com.finsight.backend.service;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.request.SignupForm;
import com.finsight.backend.enumerate.Role;
import com.finsight.backend.mapper.MemberMapper;
import com.finsight.backend.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;
    private final EmailService emailService;

    @Override
    public Optional<Member> findMember(LoginForm loginForm) {
        Member member = memberMapper.findMemberByUserIdAndPassword(loginForm);
        return Optional.ofNullable(member);
    }

    @Override
    public boolean registerMember(SignupForm signupForm) {
        if (!emailService.isEmailVerified(signupForm.getEmail())) {
            return false;
        }

        if (memberMapper.existsByUserId(signupForm.getUserId()) ||
                memberMapper.existsByNickname(signupForm.getNickname()) ||
                memberMapper.existsByEmail(signupForm.getEmail())) {
            return false;
        }

        Member member = Member.builder()
                .userId(signupForm.getUserId())
                .username(signupForm.getUsername())
                .nickname(signupForm.getNickname())
                .password(passwordEncoder.encode(signupForm.getPassword()))
                .birthday(signupForm.getBirthday())
                .email(signupForm.getEmail())
                .createdAt(LocalDateTime.now())
                .role(Role.INCOMPLETE)
                .build();

        boolean result = memberMapper.insert(member);

        if (result) {
            emailService.removeVerifiedEmail(signupForm.getEmail());  // ✅ 인증된 목록에서 제거
        }

        return result;
    }

    @Override
    public boolean checkUserId(String userId) {
        return memberMapper.existsByUserId(userId);
    }

    @Override
    public boolean checkNickname(String nickname) {
        return memberMapper.existsByNickname(nickname);
    }

    @Override
    public boolean checkEmail(String email) {
        return memberMapper.existsByEmail(email);
    }


}
