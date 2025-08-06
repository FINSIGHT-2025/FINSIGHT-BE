package com.finsight.backend.handler;

import com.finsight.backend.enumerate.ProductType;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(ProductType.class)
public class ProductTypeHandler extends GenericEnumTypeHandler<ProductType>{
    public ProductTypeHandler() {
        super(ProductType.class);
    }
}
