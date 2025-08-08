package com.finsight.backend.dto.response;

import com.finsight.backend.dto.NewsSentimentDto;
import com.finsight.backend.vo.Fund;
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
public class FundByFilterDto extends ProductByFilterDto{
    private String productCountry;
    private String productType;
    private NewsSentimentDto newsSentiment;
//    private Double productRateOfReturn;
//    private Integer fundScale;
//    private Boolean userOwns;
//    private Boolean isPopularInUserGroup;

    public static FundByFilterDto fundVoToFundByFilterDto(Fund fund, NewsSentimentDto newsSentimentDto){
        return FundByFilterDto.builder()
                .productCode(fund.getProductCode())
                .productCountry(fund.getFundCountry().getDbValue())
                .productCompanyName(fund.getProductCompanyName())
                .productType(fund.getFundType().getDbValue())
                .productName(fund.getProductName())
                .productRiskGrade(fund.getProductRiskGrade())
                .newsSentiment(newsSentimentDto)
                .build();
    }
}
