package com.finsight.backend.dto.response;

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
    // private Integer etfNav;
    // private Integer etfVolume;
    // private productRateOfReturn;
    // private List<Map<String, Integer>> newsSentiment;
    // private Boolean userOwns;
    // private Boolean isPopularInUserGroup;
    // private Boolean recommandByNews;

    public static EtfByFilterDto etfVoToEtfByFilterDto(Etf etf){
        return EtfByFilterDto.builder()
                .productCode(etf.getProductCode())
                .productCountry(etf.getEtfCountry().getDbValue())
                .productType(etf.getEtfType().getDbValue())
                .productName(etf.getProductName())
                .productRiskGrade(etf.getProductRiskGrade())
                .build();
    }
}
