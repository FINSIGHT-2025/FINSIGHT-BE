package com.finsight.backend.domain.enumerate.handler;

import com.finsight.backend.domain.enumerate.InvestmentProfileType;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(InvestmentProfileType.class)
public class InvestmentProfileTypeHandler extends GenericEnumTypeHandler<InvestmentProfileType> {
    public InvestmentProfileTypeHandler() { super(InvestmentProfileType.class); }
}