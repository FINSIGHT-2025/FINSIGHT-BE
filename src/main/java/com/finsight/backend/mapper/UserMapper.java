package com.finsight.backend.mapper;

import com.finsight.backend.vo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    boolean insert(User user);
    User findUserByUserId(String userId);

    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);

    boolean updateUserRoleToComplete(String userId);

    void deleteUser(String userId);

    void updatePassword(@Param("userId") String userId, @Param("userPassword") String userPassword);
    void updateEmail(@Param("userId") String userId, @Param("userEmail") String userEmail);
}
