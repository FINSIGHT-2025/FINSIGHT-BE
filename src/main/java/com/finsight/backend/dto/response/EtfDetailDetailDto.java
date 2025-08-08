package com.finsight.backend.dto.response;

import com.finsight.backend.enumerate.ProductCountry;
import com.finsight.backend.enumerate.ProductType;
import com.finsight.backend.vo.EAssetAllocation;
import com.finsight.backend.vo.EConstituentStocks;
import com.finsight.backend.vo.EEquityRatio;
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
    private List<EAssetAllocation> eAssetAllocation;
    private List<EEquityRatio> eEquityRatio;
    private List<EConstituentStocks> eConstituentStocks;
    private LocalDate etfListingDate;
    private Integer etfMinTradingUnit;
    private String etfTaxType;
    private List<NewsResponseDTO> etfNewsResponse;

    public static EtfDetailDetailDto etfVoToEtfDetailDto(Etf etf, List<NewsResponseDTO> etfNewsResponse){
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
                .eAssetAllocation(etf.getEAssetAllocation())
                .eEquityRatio(etf.getEEquityRatio())
                .eConstituentStocks(etf.getEConstituentStocks())
                .etfListingDate(etf.getEtfListingDate())
                .etfMinTradingUnit(etf.getEtfMinTradingUnit())
                .etfTaxType(etf.getEtfTaxType())
                .etfNewsResponse(etfNewsResponse)
                .build();
    }

}
