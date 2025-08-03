package com.finsight.backend.mapper;

import com.finsight.backend.vo.Deposit;

public interface DepositMapper {
    Deposit findDepositByCode(String productCode);
}
