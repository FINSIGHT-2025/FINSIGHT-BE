package com.finsight.backend.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvTestProfileType implements BaseEnum {
    STABLE("stable"),
    STABLEPLUS("stableplus"),
    NEUTRAL("neutral"),
    AGGRESSIVE("aggressive"),
    VERYAGGRESSIVE("veryaggressive");

    private final String dbValue;

    @Override
    public String toString() { return dbValue; }
    public static InvTestProfileType fromDbValue(String dbValue) {
        for (InvTestProfileType type : values()) {
            if (type.dbValue.equalsIgnoreCase(dbValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + dbValue);
    }
}
