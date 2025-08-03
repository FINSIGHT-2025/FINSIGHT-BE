package com.finsight.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deposit extends Product{
    private String depositJoinMember;
    private String depositSpclCnd;
    private String depositMtrtInt;
    private String depositMaxLimit;
    private String depositJoinWay;
    private String depositJoinDeny;
    private String depositEtcNote;
    private String depositOption;
}
