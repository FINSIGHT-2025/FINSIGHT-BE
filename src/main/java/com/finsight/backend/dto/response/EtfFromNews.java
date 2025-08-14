package com.finsight.backend.dto.response;

import com.finsight.backend.domain.vo.product.EtfVO;
import com.finsight.backend.dto.NewsSentimentTotalDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EtfFromNews extends ProductFromNews{
    private String productCountry;
    private String productType;
    private NewsSentimentTotalDto newsSentiment;
    private Double etfNav;

    // 카테고리 추가?
    public static EtfFromNews etfVoToEtfFromNews(EtfVO etf, NewsSentimentTotalDto newsSentimentDto, Boolean userOwn, Boolean userWatch, Double etfNav){
        return EtfFromNews.builder()
                .productCode(etf.getProductCode())
                .productCountry(etf.getEtfCountry().getDbValue())
                .productCompanyName(etf.getProductCompanyName())
                .productType(etf.getEtfType().getDbValue())
                .productName(etf.getProductName())
                .productRiskGrade(etf.getProductRiskGrade())
                .newsSentiment(newsSentimentDto)
                .userOwns(userOwn)
                .isPopularInUserGroup(Boolean.FALSE)
                .userWatches(userWatch)
                .etfNav(etfNav)
                .build();
    }
}
