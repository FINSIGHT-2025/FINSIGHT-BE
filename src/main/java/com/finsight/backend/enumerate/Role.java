package com.finsight.backend.enumerate;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    INVEST_O("invets_o"), INVEST_X("invest_x");
    private String name;
}