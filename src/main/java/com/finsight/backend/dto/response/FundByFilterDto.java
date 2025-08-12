package com.finsight.backend.dto.response;

import com.finsight.backend.dto.NewsSentimentTotalDto;
import com.finsight.backend.domain.vo.product.FundVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FundByFilterDto extends ProductByFilterDto{
    private String productCountry;
    private String productType;
    private NewsSentimentTotalDto newsSentiment;
    private Double productRateOfReturn;
    private Double fundScale;

    public static FundByFilterDto fundVoToFundByFilterDto(FundVO fund,
                                                          NewsSentimentTotalDto newsSentimentDto,
                                                          Boolean userOwn,
                                                          Boolean userWatch,
                                                          double productRateOfReturn,
                                                          double fundScale){
        return FundByFilterDto.builder()
                .productCode(fund.getProductCode())
                .productCountry(fund.getFundCountry().getDbValue())
                .productCompanyName(fund.getProductCompanyName())
                .productType(fund.getFundType().getDbValue())
                .productName(fund.getProductName())
                .productRiskGrade(fund.getProductRiskGrade())
                .newsSentiment(newsSentimentDto)
                .userOwns(userOwn)
                .isPopularInUserGroup(Boolean.FALSE)
                .userWatches(userWatch)
                .productRateOfReturn(productRateOfReturn)
                .fundScale(fundScale)
                .build();
    }
}
