package com.finsight.backend.dto.response;

import com.finsight.backend.enumerate.ProductCountry;
import com.finsight.backend.enumerate.ProductType;
import com.finsight.backend.vo.Etf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EtfDetailDetailDto extends ProductDetailDto {
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
    private List<Map<String, String>> etfAssetAllocation;
    private List<Map<String, String>> etfEquityRatio;
    private List<Map<String, String>> etfConstituentStocks;
    private LocalDate etfListingDate;
    private Integer etfMinTradingUnit;
    private String etfTaxType;

    public static EtfDetailDetailDto etfVoToEtfDetailDto(Etf etf,
                                                         List<Map<String, String>> etfAssetAllocation,
                                                         List<Map<String, String>> etfEquityRatio,
                                                         List<Map<String, String>> etfConstituentStocks){
        return EtfDetailDetailDto.builder()
                .productCode(etf.getProductCode())
                .productName(etf.getProductName())
                .productCompanyName(etf.getProductCompanyName())
                .productRiskGrade(etf.getProductRiskGrade())
                .etfCountry(etf.getEtfCountry())
                .etfType(etf.getEtfType())
                .etfDelistingStatus(etf.getEtfDelistingStatus())
                .etfNetAssets(etf.getEtfNetAssets())
                .etfFundCharacteristics(etf.getEtfFundCharacteristics())
                .etfManagementStrategy(etf.getEtfManagementStrategy())
                .etfTotalExpenseRatio(etf.getEtfTotalExpenseRatio())
                .etfCollectiveInvestmentAgreementUrl(etf.getEtfCollectiveInvestmentAgreementUrl())
                .etfInvestmentProspectusUrl(etf.getEtfInvestmentProspectusUrl())
                .etfSimplifiedProspectusUrl(etf.getEtfSimplifiedProspectusUrl())
                .etfBenchmarkIndex(etf.getEtfBenchmarkIndex())
                .etfAssetAllocation(etfAssetAllocation)
                .etfEquityRatio(etfEquityRatio)
                .etfConstituentStocks(etfConstituentStocks)
                .etfListingDate(etf.getEtfListingDate())
                .etfMinTradingUnit(etf.getEtfMinTradingUnit())
                .etfTaxType(etf.getEtfTaxType())
                .build();
    }

}
