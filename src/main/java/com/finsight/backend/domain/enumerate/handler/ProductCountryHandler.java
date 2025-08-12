package com.finsight.backend.domain.enumerate.handler;

import com.finsight.backend.domain.enumerate.ProductCountry;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(ProductCountry.class)
public class ProductCountryHandler extends GenericEnumTypeHandler<ProductCountry> {
    public ProductCountryHandler() {
        super(ProductCountry.class);
    }
}
