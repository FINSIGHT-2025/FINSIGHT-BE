package com.finsight.backend.domain.vo.product;

import com.finsight.backend.domain.enumerate.ProductCountry;
import com.finsight.backend.domain.enumerate.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtfVO extends ProductVO {
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

    private List<EAssetAllocationVO> eAssetAllocation;
    private List<EEquityRatioVO> eEquityRatio;
    private List<EConstituentStocksVO> eConstituentStocks;

    // private List<NewsProduct> newsProduct;
}
