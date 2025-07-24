package com.finsight.backend.service;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.mapper.MemberMapper;
import com.finsight.backend.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final MemberMapper memberMapper;

    @Override
    public Optional<Member> findMember(LoginForm loginForm) {
        Member member = memberMapper.findMemberByUserIdAndPassword(loginForm);
        return Optional.ofNullable(member);
    }
}
