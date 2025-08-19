package com.finsight.backend.service.product.handler;

import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.domain.vo.product.ProductVO;

import java.util.List;

public interface ProductDtoHandler<T extends ProductVO> {
    Class<T> getProductType();
    ProductDetailDto toDetailDto(T product);
    List<ProductByFilterDto> toFilterDto(List<T> product, String userId, String sort);

    ProductByFilterDto recommendProductDto(T product, String userId);
}
