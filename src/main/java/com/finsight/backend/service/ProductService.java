package com.finsight.backend.service;

import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.vo.Product;

import java.util.List;

public interface ProductService {
    <T extends Product> ProductDetailDto findProduct(String productCode, Class<T> expectedType);

    <T extends Product> List<ProductByFilterDto> findProductByFilter(Class<T> expectedType,
                                                                     String sort,
                                                                     String country,
                                                                     String type,
                                                                     Integer riskGrade);
}