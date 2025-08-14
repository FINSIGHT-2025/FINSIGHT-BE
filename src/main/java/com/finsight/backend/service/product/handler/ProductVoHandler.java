package com.finsight.backend.service.product.handler;

import com.finsight.backend.domain.vo.product.ProductVO;

import java.util.List;

public interface ProductVoHandler<T extends ProductVO>{
    T findProduct(String productCode);
    Class<T> getProductType();
    List<T> findProductListByFilter(String sort, String country, String type, Boolean isMatched, String userId);
}
