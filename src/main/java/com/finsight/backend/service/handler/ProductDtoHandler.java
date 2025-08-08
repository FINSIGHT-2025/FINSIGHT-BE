package com.finsight.backend.service.handler;

import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.vo.Product;

import java.util.List;

public interface ProductDtoHandler<T extends Product> {
    Class<T> getProductType();
    ProductDetailDto toDetailDto(T product);
    List<ProductByFilterDto> toFilterDto(List<T> product, String userId);
}
