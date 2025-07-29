package com.finsight.backend.handler;

import com.finsight.backend.enumerate.UserRole;
import com.finsight.backend.config.mybatis.GenericEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(UserRole.class)
public class RoleTypeHandler extends GenericEnumTypeHandler<UserRole> {
    public RoleTypeHandler() {
        super(UserRole.class);
    }
}
