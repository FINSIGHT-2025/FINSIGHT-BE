package com.finsight.backend.handler;

import com.finsight.backend.enumerate.InvTestProfileType;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(InvTestProfileType.class)
public class RiskTypeHandler extends GenericEnumTypeHandler<InvTestProfileType> {
    public RiskTypeHandler() { super(InvTestProfileType.class); }
}