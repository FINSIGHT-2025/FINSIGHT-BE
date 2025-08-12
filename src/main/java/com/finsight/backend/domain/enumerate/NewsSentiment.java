package com.finsight.backend.domain.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NewsSentiment implements BaseEnum{
    POSITIVE("positive"), NEGATIVE("negative"), NEUTRAL("neutral");
    private final String dbValue;

    @Override
    public String getDbValue() {
        return dbValue;
    }
    public static NewsSentiment fromDbValue(String dbValue){
        for (NewsSentiment newsSentiment : values()) {
            if(newsSentiment.getDbValue().equals(dbValue)){
                return newsSentiment;
            }
        }
        throw new IllegalArgumentException("Unknown dbValue: " + dbValue);
    }


}
