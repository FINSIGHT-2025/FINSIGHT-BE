package com.finsight.backend.enumerate;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
//@AllArgsConstructor
public enum Role {
    INCOMPLETE("incomplete"),
    COMPLETE("complete");

    private final String dbValue;

    Role(String dbValue) {
        this.dbValue = dbValue;
    }

    @Override
    public String toString() {
        return dbValue;
    }
}
