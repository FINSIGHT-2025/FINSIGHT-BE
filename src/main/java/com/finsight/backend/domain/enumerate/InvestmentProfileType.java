package com.finsight.backend.domain.enumerate;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvestmentProfileType implements BaseEnum {
    STABLE("stable"),
    STABLEPLUS("stableplus"),
    NEUTRAL("neutral"),
    AGGRESSIVE("aggressive"),
    VERYAGGRESSIVE("veryaggressive");

    @JsonValue
    private final String dbValue;

    @Override
    public String toString() { return dbValue; }

    public static InvestmentProfileType fromDbValue(String dbValue) {
        for (InvestmentProfileType type : values()) {
            if (type.dbValue.equalsIgnoreCase(dbValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown dbValue: " + dbValue);
    }
}
