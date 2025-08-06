package com.finsight.backend.mapper;

import org.apache.ibatis.annotations.Param;

public interface ProductCategoryMapper {
    String findProductCategory(@Param("productCode") String productCode);
}

