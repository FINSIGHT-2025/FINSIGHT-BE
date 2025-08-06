package com.finsight.backend.handler;

import com.finsight.backend.enumerate.ProductCountry;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(ProductCountry.class)
public class ProductCountryHandler extends GenericEnumTypeHandler<ProductCountry> {
    public ProductCountryHandler() {
        super(ProductCountry.class);
    }
}
