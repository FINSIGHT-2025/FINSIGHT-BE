package com.finsight.backend.mapper;

import com.finsight.backend.config.CorsConfig;
import com.finsight.backend.config.MongoConfig;
import com.finsight.backend.config.RootConfig;
import com.finsight.backend.domain.enumerate.ProductCountry;
import com.finsight.backend.domain.enumerate.ProductType;
import com.finsight.backend.repository.mapper.FundMapper;
import com.finsight.backend.domain.vo.product.FAssetAllocationVO;
import com.finsight.backend.domain.vo.product.FStockHoldingsVO;
import com.finsight.backend.domain.vo.product.FundVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@Slf4j
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {
        RootConfig.class,
        CorsConfig.class,
        MongoConfig.class
})
class FundMapperTest {
    @Autowired
    private FundMapper fundMapper;

    @Test
    void findFundByCode() {
        FundVO fund = fundMapper.findFundByCode("K55301BB5738");

        Assertions.assertEquals("K55301BB5738", fund.getProductCode());

        List<FAssetAllocationVO> fAssetAllocation = fund.getFAssetAllocation();
        List<FStockHoldingsVO> fStockHoldings = fund.getFStockHoldings();

        Assertions.assertNotNull(fAssetAllocation);
        Assertions.assertNotNull(fStockHoldings);
    }

    @Test
    void findFundListOrderByFilter() {
        List<FundVO> noFilterFund = fundMapper.findFundListByFilter(null, null, null);
        List<FundVO> fundListOrderByFilter = fundMapper.findFundListByFilter(ProductCountry.DOMESTIC, ProductType.EQUITY, new Integer[]{1,2,3,4,5,6});

        Assertions.assertEquals(60, noFilterFund.size());
        System.out.println("fundListOrderByFilter.size() = " + fundListOrderByFilter.size());
        for (FundVO fund : fundListOrderByFilter) {
            Assertions.assertEquals(1, fund.getProductRiskGrade());
            Assertions.assertEquals(ProductCountry.DOMESTIC, fund.getFundCountry());
            Assertions.assertEquals(ProductType.EQUITY, fund.getFundType());
            log.info("[findFundListOrderByFilter] Fund : {}", fund);
        }
    }
}