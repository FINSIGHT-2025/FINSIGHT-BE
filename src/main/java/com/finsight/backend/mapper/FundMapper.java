package com.finsight.backend.mapper;

import com.finsight.backend.vo.Fund;

public interface FundMapper {
    Fund findFundByCode(String productCode);
}