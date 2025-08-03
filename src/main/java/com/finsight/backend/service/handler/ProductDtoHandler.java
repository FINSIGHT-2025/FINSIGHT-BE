package com.finsight.backend.service.handler;

import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.vo.Product;

public interface ProductDtoHandler<T extends Product> {
    Class<T> getProductType();
    ProductDetailDto toDetailDto(T product);
}
