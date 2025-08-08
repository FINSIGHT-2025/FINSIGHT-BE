package com.finsight.backend.mapper;

import com.finsight.backend.vo.Deposit;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DepositMapper {
    Deposit findDepositByCode(String productCode);

    List<Deposit> findDepositListOrderByIntrRate(@Param("limit") Integer limit, @Param("offset") Integer offset);
    List<Deposit> findDepositListOrderByIntrRate2(@Param("limit") Integer limit, @Param("offset") Integer offset);
}
