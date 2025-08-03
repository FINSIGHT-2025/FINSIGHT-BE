package com.finsight.backend.mapper;

import com.finsight.backend.vo.Deposit;

import java.util.List;

public interface DepositMapper {
    Deposit findDepositByCode(String productCode);
    List<Deposit> findDepositByFilter(String sort, String country, String type, Integer riskGrade);
}
