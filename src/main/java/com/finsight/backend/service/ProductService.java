package com.finsight.backend.service;

import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.vo.Product;

import java.util.List;

public interface ProductService {
    <T extends Product, D extends ProductDetailDto> D findProduct(String productCode, Class<T> expectedType, Class<D> expectedDtoType);

    <T extends Product> List<T> findProductByFilter(Class<T> expectedType,
                                                    String sort,
                                                    String country,
                                                    String type,
                                                    String riskGrade);
}