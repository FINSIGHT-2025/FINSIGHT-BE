package com.finsight.backend.mapper;

import com.finsight.backend.vo.User;

public interface UserMapper {
    boolean insert(User user);
    User findUserByUserId(String userId);

    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);

    boolean updateUserRoleToComplete(String userId);
}
