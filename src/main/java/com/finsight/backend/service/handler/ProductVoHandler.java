package com.finsight.backend.service.handler;

import com.finsight.backend.vo.Product;

import java.util.List;

public interface ProductVoHandler<T extends Product>{
    T findProduct(String productCode);
    Class<T> getProductType();
    List<T> findProductListByFilter(String sort, String country, String type, Boolean isMatched, String userId);
}
