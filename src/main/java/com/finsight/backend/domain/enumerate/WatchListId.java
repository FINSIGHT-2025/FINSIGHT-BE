package com.finsight.backend.domain.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WatchListId implements BaseEnum {
//`product_category` ENUM('deposit', 'fund', 'etf') NOT NULL COMMENT '카테고리',
    DEPOSIT("deposit"),
    FUND("fund"),
    ETF("etf");

    private final String dbValue;

    @Override
    public String toString() {
        return dbValue;
    }
    public static WatchListId fromDbValue(String dbValue) {
        for (WatchListId type : values()) {
            if (type.dbValue.equals(dbValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + dbValue);
    }
}
