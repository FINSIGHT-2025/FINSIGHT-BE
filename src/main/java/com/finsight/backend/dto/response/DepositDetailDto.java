package com.finsight.backend.dto.response;

import com.finsight.backend.domain.vo.product.DOptionVO;
import com.finsight.backend.domain.vo.product.DepositVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

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
    private List<DOptionVO> dOptionVO;
    private Double dOptionIntrRate;
    private Double dOptionIntrRate2;

    public static DepositDetailDto depositVoToDepositDetailDto(DepositVO deposit, Double dOptionIntrRate, Double dOptionIntrRate2){
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
                .dOptionVO(deposit.getDOption())
                .dOptionIntrRate(dOptionIntrRate)
                .dOptionIntrRate2(dOptionIntrRate2)
                .build();
    }
}
