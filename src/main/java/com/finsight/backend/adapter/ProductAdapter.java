package com.finsight.backend.adapter;

import com.finsight.backend.dto.response.DepositDetailDto;
import com.finsight.backend.dto.response.EtfDetailDetailDto;
import com.finsight.backend.dto.response.FundDetailDetailDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.enumerate.ProductType;
import com.finsight.backend.vo.Deposit;
import com.finsight.backend.vo.Etf;
import com.finsight.backend.vo.Fund;
import com.finsight.backend.vo.Product;

import java.util.Map;

public class ProductAdapter {

    private static final Map<String, Class<? extends Product>> productTypeMap = Map.of(
            "deposit", Deposit.class,
            "fund", Fund.class,
            "etf", Etf.class
    );

    private static final Map<String, Class<? extends ProductDetailDto>> productDetailTypeMap = Map.of(
            "deposit", DepositDetailDto.class,
            "fund", FundDetailDetailDto.class,
            "etf", EtfDetailDetailDto.class
    );

    public static Class<? extends Product> productType(String category){
        return productTypeMap.get(category.toLowerCase());
    }
    public static Class<? extends ProductDetailDto> productDetailDtoType(String category){
        return productDetailTypeMap.get(category);
    }
}
