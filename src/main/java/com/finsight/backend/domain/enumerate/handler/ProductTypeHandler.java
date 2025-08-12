package com.finsight.backend.domain.enumerate.handler;

import com.finsight.backend.domain.enumerate.ProductType;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(ProductType.class)
public class ProductTypeHandler extends GenericEnumTypeHandler<ProductType>{
    public ProductTypeHandler() {
        super(ProductType.class);
    }
}
