package com.finsight.backend.domain.enumerate.handler;

import com.finsight.backend.domain.enumerate.UserRole;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(UserRole.class)
public class UserRoleTypeHandler extends GenericEnumTypeHandler<UserRole> {
    public UserRoleTypeHandler() {
        super(UserRole.class);
    }
}
