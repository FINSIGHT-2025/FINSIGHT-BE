package com.finsight.backend.dto.response;

import com.finsight.backend.domain.vo.product.FundVO;
import com.finsight.backend.dto.NewsSentimentTotalDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FundByWatchDto extends ProductByWatchDto{
    private String productCountry;
    private String productType;
    private NewsSentimentTotalDto newsSentiment;
    private Double productRateOfReturn;
    private Double fundScale;

    public static FundByWatchDto fundVoToFundByWatchDto(FundVO fund,
                                                          NewsSentimentTotalDto newsSentimentDto,
                                                          double productRateOfReturn,
                                                          double fundScale){
        return FundByWatchDto.builder()
                .productCode(fund.getProductCode())
                .productCountry(fund.getFundCountry().getDbValue())
                .productCompanyName(fund.getProductCompanyName())
                .productType(fund.getFundType().getDbValue())
                .productName(fund.getProductName())
                .productRiskGrade(fund.getProductRiskGrade())
                .newsSentiment(newsSentimentDto)
                .isPopularInUserGroup(Boolean.FALSE)
                .productRateOfReturn(productRateOfReturn)
                .fundScale(fundScale)
                .build();
    }
}
