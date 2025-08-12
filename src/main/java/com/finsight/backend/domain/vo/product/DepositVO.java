package com.finsight.backend.domain.vo.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositVO extends ProductVO {
    private String depositJoinMember;
    private String depositSpclCnd;
    private String depositMtrtInt;
    private String depositMaxLimit;
    private String depositJoinWay;
    private String depositJoinDeny;
    private String depositEtcNote;

    private List<DOptionVO> dOption;
}
