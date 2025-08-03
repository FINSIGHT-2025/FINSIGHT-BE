package com.finsight.backend.vo;

import com.finsight.backend.enumerate.ProductCountry;
import com.finsight.backend.enumerate.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Etf extends Product{
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
    private String etfAssetAllocation;
    private String etfEquityRatio;
    private String etfConstituentStocks;
    private LocalDate etfListingDate;
    private Integer etfMinTradingUnit;
    private String etfTaxType;
}
