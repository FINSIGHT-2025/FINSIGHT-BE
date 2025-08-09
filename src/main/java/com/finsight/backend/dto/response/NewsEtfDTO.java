package com.finsight.backend.dto.response;

import com.finsight.backend.enumerate.ProductCountry;
import com.finsight.backend.enumerate.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewsEtfDTO extends ProductDetailDto {
    private ProductCountry etfCountry;
    private ProductType etfType;
    private Boolean etfDelistingStatus;
    private Double etfNetAssets;
    private String etfFundCharacteristics;
    private String etfManagementStrategy;
    private Double etfTotalExpenseRatio;
    private String etfCollectiveInvestmentAgreementUrl;
    private String etfInvestmentProspectusUrl;
    private String etfSimplifiedProspectusUrl;
    private String etfBenchmarkIndex;
    private LocalDate etfListingDate;
    private Integer etfMinTradingUnit;
    private String etfTaxType;

    private final String productCategory = "etf";
    private NewsSentiment newsSentiment;

}
