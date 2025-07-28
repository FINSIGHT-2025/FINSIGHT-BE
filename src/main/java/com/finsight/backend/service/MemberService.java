package com.finsight.backend.service;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.request.SignupForm;
import com.finsight.backend.vo.Member;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findMember(LoginForm loginForm);
    boolean registerMember(SignupForm signupForm);

    boolean checkUserId(String userId);
    boolean checkNickname(String nickname);
    boolean checkEmail(String email);
}
