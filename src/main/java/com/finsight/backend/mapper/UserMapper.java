package com.finsight.backend.mapper;

import com.finsight.backend.vo.Member;
import org.apache.ibatis.annotations.Mapper;

/*
MyBatis의 Mapper 인터페이스로, DB 쿼리와 연결
 */
@Mapper
public interface UserMapper {
    int countByUserId(String userId);
    int countByNickname(String nickname);
    int countByEmail(String email);
    int insertMember(Member member);
}

