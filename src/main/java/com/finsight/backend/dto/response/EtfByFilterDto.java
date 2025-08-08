package com.finsight.backend.dto.response;

import com.finsight.backend.dto.NewsSentimentDto;
import com.finsight.backend.vo.Etf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EtfByFilterDto extends ProductByFilterDto{
    private String productCountry;
    private String productType;
    private NewsSentimentDto newsSentiment;

    public static EtfByFilterDto etfVoToEtfByFilterDto(Etf etf, NewsSentimentDto newsSentimentDto, Boolean userOwn, Boolean userWatch){
        return EtfByFilterDto.builder()
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
                .build();
    }
}
