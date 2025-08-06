package com.finsight.backend.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ProductCountry implements BaseEnum{
    DOMESTIC("domestic"), FOREIGN("foreign");

    private final String dbValue;

    @Override
    public String getDbValue(){
        return dbValue;
    }
    public static ProductCountry fromDbValue(String dbValue){
        for (ProductCountry productCountry : values()) {
            if(productCountry.getDbValue().equals(dbValue)){
                return productCountry;
            }
        }
        throw new IllegalArgumentException("Unknown dbValue: " + dbValue);
    }
}
