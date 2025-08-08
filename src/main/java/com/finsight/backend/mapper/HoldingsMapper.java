package com.finsight.backend.mapper;

import com.finsight.backend.vo.Holdings;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HoldingsMapper {
    Holdings findByUserAndProduct(@Param("userId") String userId, @Param("productCode") String productCode);
    void insert(Holdings holdings);
    void update(Holdings holdings);

    // MP
    Double selectDomesticEquity2ByUserId(String userId);
    Double selectDomesticMixed5ByUserId(String userId);
    Double selectDomesticBond5ByUserId(String userId);
    Double selectDomesticBond6ByUserId(String userId);
    Double selectDepositPriceByUserId(String userId);
    Double selectForeignEquity1ByUserId(String userId);
    Double selectForeignEquity2ByUserId(String userId);
    Double selectForeignEquity3ByUserId(String userId);
    Double selectForeignBond4ByUserId(String userId);

    // 보유내역
    Double selectDepositByUserId(String userId);
    Double selectDomesticByUserId(String userId);
    Double selectForeignByUserId(String userId);

    // userOwns 사용
    Boolean existProductByUserIdAndProductCode(@Param("userId") String userId, @Param("productCode") String productCode);

}

