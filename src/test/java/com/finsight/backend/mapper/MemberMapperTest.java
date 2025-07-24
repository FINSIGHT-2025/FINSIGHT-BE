package com.finsight.backend.mapper;

import com.finsight.backend.config.RootConfig;
import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.vo.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RootConfig.class})
class MemberMapperTest {
    @Autowired
    private MemberMapper memberMapper;

    @Test
    void findMemberByUserIdAndPassword() {
        LoginForm loginForm = new LoginForm("user123", "securepassword123!");
        Member findMember = memberMapper.findMemberByUserIdAndPassword(loginForm);
        Assertions.assertEquals("홍길동", findMember.getUsername());
    }
}