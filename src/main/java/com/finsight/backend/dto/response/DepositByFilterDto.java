package com.finsight.backend.dto.response;

import com.finsight.backend.dto.NewsSentimentDto;
import com.finsight.backend.vo.Deposit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DepositByFilterDto extends ProductByFilterDto{
    private Double depositIntrRate;
    private Double depositIntrRate2;

    public static DepositByFilterDto depositVoToDepositByFilterDto(Deposit deposit,
                                                                   Double depositIntrRate,
                                                                   Double depositIntrRate2,
                                                                   Boolean userOwn){
        return DepositByFilterDto.builder()
                .productCode(deposit.getProductCode())
                .productName(deposit.getProductName())
                .productCompanyName(deposit.getProductCompanyName())
                .productRiskGrade(deposit.getProductRiskGrade())
                .depositIntrRate(depositIntrRate)
                .depositIntrRate2(depositIntrRate2)
                .userOwns(userOwn)
                .build();
    }
}
