package com.finsight.backend.repository.mapper;

import com.finsight.backend.domain.vo.user.UserVO;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    boolean insert(UserVO user);
    UserVO findUserByUserId(String userId);

    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);

    boolean updateUserRoleToComplete(String userId);

    boolean deleteUser(String userId);

    void updatePassword(@Param("userId") String userId, @Param("userPassword") String userPassword);
    void updateEmail(@Param("userId") String userId, @Param("userEmail") String userEmail);
}
