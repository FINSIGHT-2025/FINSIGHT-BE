package com.finsight.backend.repository.mapper;

import org.apache.ibatis.annotations.Param;

public interface ProductCategoryMapper {
    String findProductCategory(@Param("productCode") String productCode);
}

