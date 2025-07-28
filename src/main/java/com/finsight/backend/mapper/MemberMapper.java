package com.finsight.backend.mapper;

import com.finsight.backend.dto.request.LoginForm;
import com.finsight.backend.vo.Member;

public interface MemberMapper {
    boolean insert(Member member);
    Member findMemberByUserId(String userId);

    boolean existsByUserId(String userId);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
}
