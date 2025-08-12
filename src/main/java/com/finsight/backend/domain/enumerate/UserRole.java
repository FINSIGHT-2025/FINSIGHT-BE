package com.finsight.backend.domain.enumerate;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole implements BaseEnum{
    INCOMPLETE("incomplete"),
    COMPLETE("complete");

    private final String dbValue;

    @Override
    public String getDbValue() {
        return dbValue;
    }

    public static UserRole fromDbValue(String dbValue){
        for (UserRole userRole : values()) {
            if(userRole.getDbValue().equals(dbValue)){
                return userRole;
            }
        }
        throw new IllegalArgumentException("Unknown dbValue: " + dbValue);
    }
}
