package com.finsight.backend.enumerate;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    INVEST_O("invets_o"), INVEST_X("invest_x");
    private final String name;
}