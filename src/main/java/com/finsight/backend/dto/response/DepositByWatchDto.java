package com.finsight.backend.dto.response;

import com.finsight.backend.domain.vo.product.DepositVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DepositByWatchDto extends ProductByWatchDto {
    private Double depositIntrRate;
    private Double depositIntrRate2;

    public static DepositByWatchDto depositVoToDepositByWatchDto(DepositVO deposit,
                                                                   Double depositIntrRate,
                                                                   Double depositIntrRate2){
        return DepositByWatchDto.builder()
                .productCode(deposit.getProductCode())
                .productName(deposit.getProductName())
                .productCompanyName(deposit.getProductCompanyName())
                .productRiskGrade(deposit.getProductRiskGrade())
                .depositIntrRate(depositIntrRate)
                .depositIntrRate2(depositIntrRate2)
                .isPopularInUserGroup(Boolean.FALSE)
                .build();
    }
}
