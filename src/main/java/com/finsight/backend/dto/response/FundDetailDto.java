package com.finsight.backend.dto.response;

import com.finsight.backend.domain.enumerate.ProductCountry;
import com.finsight.backend.domain.enumerate.ProductType;
import com.finsight.backend.domain.vo.product.FAssetAllocationVO;
import com.finsight.backend.domain.vo.product.FStockHoldingsVO;
import com.finsight.backend.domain.vo.product.FundVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FundDetailDto extends ProductDetailDto {
    private ProductCountry fundCountry;
    private ProductType fundType;
    private Boolean fundDelistingStatus;
    private String fundFeature;
    private String fundManagementStrategy;
    private String fundFeeTotalExpenseRatio;
    private String fundFeeBackEndLoad;
    private String fundFeeRedemption;
    private String fundReportInvestment;
    private String fundReportCollectiveInvestmentTermsUrl;
    private String fundReportInvestmentProspectusUrl;
    private String fundReportSimplidfiedProspectusUrl;
    private List<FAssetAllocationVO> fAssetAllocation;
    private List<FStockHoldingsVO> fStockHoldings;
    private String fundFeeFrontEndLoad;
    private String fundEstablishedDate;
    private List<NewsResponseDTO> fundNewsResponse;
    
    private FundPriceSummaryDto fundPriceSummaryDto;

    public static FundDetailDto fundVoToFundDetailDto(FundVO fund, List<NewsResponseDTO> fundNewsResponse, FundPriceSummaryDto fundPriceSummary){
        return FundDetailDto.builder()
                .productCode(fund.getProductCode())
                .productName(fund.getProductName())
                .productCompanyName(fund.getProductCompanyName())
                .productRiskGrade(fund.getProductRiskGrade())
                .fundCountry(fund.getFundCountry())
                .fundType(fund.getFundType())
                .fundDelistingStatus(fund.getFundDelistingStatus())
                .fundFeature(fund.getFundFeature())
                .fundManagementStrategy(fund.getFundManagementStrategy())
                .fundFeeTotalExpenseRatio(fund.getFundFeeTotalExpenseRatio())
                .fundFeeBackEndLoad(fund.getFundFeeBackEndLoad())
                .fundFeeRedemption(fund.getFundFeeRedemption())
                .fundReportInvestment(fund.getFundReportInvestment())
                .fundReportCollectiveInvestmentTermsUrl(fund.getFundReportCollectiveInvestmentTermsUrl())
                .fundReportInvestmentProspectusUrl(fund.getFundReportInvestmentProspectusUrl())
                .fundReportSimplidfiedProspectusUrl(fund.getFundReportSimplidfiedProspectusUrl())
                .fAssetAllocation(fund.getFAssetAllocation())
                .fStockHoldings(fund.getFStockHoldings())
                .fundFeeFrontEndLoad(fund.getFundFeeFrontEndLoad())
                .fundEstablishedDate(fund.getFundEstablishedDate())
                .fundNewsResponse(fundNewsResponse)
                .fundPriceSummaryDto(fundPriceSummary)
                .build();
    }
}
