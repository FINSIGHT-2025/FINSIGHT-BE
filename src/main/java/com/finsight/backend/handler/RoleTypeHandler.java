package com.finsight.backend.handler;

import com.finsight.backend.enumerate.Role;
import com.finsight.backend.config.mybatis.GenericEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(Role.class)
public class RoleTypeHandler extends GenericEnumTypeHandler<Role> {
    public RoleTypeHandler() {
        super(Role.class);
    }
}
