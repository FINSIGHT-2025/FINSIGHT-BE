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
public class FundFromNews extends ProductFromNews{
    private String productCountry;
    private String productType;
    private NewsSentimentTotalDto newsSentiment;
    private Double productRateOfReturn;
    private Double fundScale;
    // 카테고리 추가?
    public static FundFromNews fundVoToFundFromNews(FundVO fund,
                                                          NewsSentimentTotalDto newsSentimentDto,
                                                          Boolean userOwn,
                                                          Boolean userWatch,
                                                          double productRateOfReturn,
                                                          double fundScale){
        return FundFromNews.builder()
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
