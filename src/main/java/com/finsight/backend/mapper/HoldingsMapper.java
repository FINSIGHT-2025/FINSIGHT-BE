package com.finsight.backend.mapper;

import com.finsight.backend.vo.Holdings;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HoldingsMapper {
    Holdings findByUserAndProduct(@Param("userId") String userId, @Param("productCode") String productCode);
    void insert(Holdings holdings);
    void update(Holdings holdings);

    String selectDepositPriceByUserId(String userId);
}

