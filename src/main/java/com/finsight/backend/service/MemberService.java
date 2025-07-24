package com.finsight.backend.service;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.vo.Member;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findMember(LoginForm loginForm);
}
