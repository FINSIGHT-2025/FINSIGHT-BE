package com.finsight.backend.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType implements BaseEnum{
    EQUITY("equity"), MIXED("mixed"), BOND("bond");
    private final String dbValue;

    @Override
    public String getDbValue() {
        return dbValue;
    }
    public static ProductType fromDbValue(String dbValue){
        for (ProductType productType : values()) {
            if(productType.getDbValue().equals(dbValue)){
                return productType;
            }
        }
        throw new IllegalArgumentException("Unknown dbValue: " + dbValue);
    }
}
