package com.finsight.backend.vo;

import com.finsight.backend.enumerate.ProductCountry;
import com.finsight.backend.enumerate.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fund extends Product{
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
    private String fundFeeFrontEndLoad;
    private String fundEstablishedDate;

    private List<FAssetAllocation> fAssetAllocation;
    private List<FStockHoldings> fStockHoldings;

    // private List<NewsProduct> newsProduct;
}
