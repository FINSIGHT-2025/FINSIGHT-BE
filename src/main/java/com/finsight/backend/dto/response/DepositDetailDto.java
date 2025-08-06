package com.finsight.backend.dto.response;

import com.finsight.backend.vo.DOption;
import com.finsight.backend.vo.Deposit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DepositDetailDto extends ProductDetailDto{
    private String depositJoinMember;
    private String depositSpclCnd;
    private String depositMtrtInt;
    private String depositMaxLimit;
    private String depositJoinWay;
    private String depositJoinDeny;
    private String depositEtcNote;
    private List<DOption> dOption;

    public static DepositDetailDto depositVoToDepositDetailDto(Deposit deposit){
        return DepositDetailDto.builder()
                .productCode(deposit.getProductCode())
                .productName(deposit.getProductName())
                .productCompanyName(deposit.getProductCompanyName())
                .productRiskGrade(deposit.getProductRiskGrade())
                .depositJoinMember(deposit.getDepositJoinMember())
                .depositSpclCnd(deposit.getDepositSpclCnd())
                .depositMtrtInt(deposit.getDepositMtrtInt())
                .depositMaxLimit(deposit.getDepositMaxLimit())
                .depositJoinWay(deposit.getDepositJoinWay())
                .depositJoinDeny(deposit.getDepositJoinDeny())
                .depositEtcNote(deposit.getDepositEtcNote())
                .dOption(deposit.getDOption())
                .build();
    }
}
