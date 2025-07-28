package com.finsight.backend.enumerate;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role implements BaseEnum{
    INCOMPLETE("incomplete"),
    COMPLETE("complete");

    private final String dbValue;

    @Override
    public String toString() {
        return dbValue;
    }
    public static Role fromDbValue(String dbValue) {
        for (Role role : values()) {
            if (role.dbValue.equalsIgnoreCase(dbValue)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + dbValue);
    }
}
