package com.finsight.backend.mapper;

import com.finsight.backend.vo.Deposit;

import java.util.List;


public interface DepositMapper {
    Deposit findDepositByCode(String productCode);

    List<Deposit> findDepositListOrderByIntrRate();
    List<Deposit> findDepositListOrderByIntrRate2();
}
