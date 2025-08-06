package com.finsight.backend.mapper;

import com.finsight.backend.enumerate.ProductCountry;
import com.finsight.backend.enumerate.ProductType;
import com.finsight.backend.vo.Fund;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FundMapper {
    Fund findFundByCode(String productCode);
    List<Fund> findFundListByFilter(@Param("productCountry") ProductCountry productCountry,
                                         @Param("productType")ProductType productType,
                                         @Param("productRiskGrade") Integer productRiskGrade);
}