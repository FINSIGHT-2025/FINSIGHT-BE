package com.finsight.backend.mapper;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.dto.request.SignupForm;
import com.finsight.backend.vo.Member;
import org.apache.ibatis.annotations.Param;

public interface MemberMapper {
    boolean insert(SignupForm signupForm);
    Member findMemberByUserIdAndPassword(LoginForm loginForm);
    boolean update(@Param("beforeNickname") String beforeNickname, @Param("afterNickname") String afterNickname);
    boolean delete(LoginForm loginForm);
}
