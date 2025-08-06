package com.finsight.backend.dto.response;

import com.finsight.backend.enumerate.ProductCountry;
import com.finsight.backend.enumerate.ProductType;
import com.finsight.backend.vo.FAssetAllocation;
import com.finsight.backend.vo.FStockHoldings;
import com.finsight.backend.vo.Fund;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FundDetailDetailDto extends ProductDetailDto {
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
    private List<FAssetAllocation> fAssetAllocation;
    private List<FStockHoldings> fStockHoldings;
    private String fundFeeFrontEndLoad;
    private String fundEstablishedDate;

    public static FundDetailDetailDto fundVoToFundDetailDto(Fund fund){
        return FundDetailDetailDto.builder()
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
                .build();
    }
}
