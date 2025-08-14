package com.finsight.backend.service.product;

import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.domain.vo.product.ProductVO;

import java.util.List;

public interface ProductService {
    <T extends ProductVO> ProductDetailDto findProduct(String productCode, Class<T> expectedType, String userId);

    <T extends ProductVO> List<ProductByFilterDto> findProductByFilter(Class<T> expectedType,
                                                                       String sort,
                                                                       String country,
                                                                       String type,
                                                                       String userId,
                                                                       Boolean isMatched);
}