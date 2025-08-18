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
public class EtfByWatchDto extends ProductByWatchDto {
    private String productCountry;
    private String productType;
    private NewsSentimentTotalDto newsSentiment;
    private Double etfNav;

    public static EtfByWatchDto etfVoToEtfByWatchDto(EtfVO etf,
                                                       NewsSentimentTotalDto newsSentimentDto,
                                                       Double etfNav){
        return EtfByWatchDto.builder()
                .productCode(etf.getProductCode())
                .productCountry(etf.getEtfCountry().getDbValue())
                .productCompanyName(etf.getProductCompanyName())
                .productType(etf.getEtfType().getDbValue())
                .productName(etf.getProductName())
                .productRiskGrade(etf.getProductRiskGrade())
                .newsSentiment(newsSentimentDto)
                .isPopularInUserGroup(Boolean.FALSE)
                .etfNav(etfNav)
                .build();
    }
}
