package com.finsight.backend.dto.response;

import com.finsight.backend.domain.enumerate.ProductCountry;
import com.finsight.backend.domain.enumerate.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewsFundDTO extends ProductDetailDto{
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

    private final String productCategory = "fund";
    private NewsSentiment newsSentiment;
}
