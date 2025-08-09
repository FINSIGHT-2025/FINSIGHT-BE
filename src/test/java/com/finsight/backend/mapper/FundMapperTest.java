package com.finsight.backend.mapper;

import com.finsight.backend.config.CorsConfig;
import com.finsight.backend.config.MongoConfig;
import com.finsight.backend.config.RootConfig;
import com.finsight.backend.enumerate.ProductCountry;
import com.finsight.backend.enumerate.ProductType;
import com.finsight.backend.vo.FAssetAllocation;
import com.finsight.backend.vo.FStockHoldings;
import com.finsight.backend.vo.Fund;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        Fund fund = fundMapper.findFundByCode("K55301BB5738");

        Assertions.assertEquals("K55301BB5738", fund.getProductCode());

        List<FAssetAllocation> fAssetAllocation = fund.getFAssetAllocation();
        List<FStockHoldings> fStockHoldings = fund.getFStockHoldings();

        Assertions.assertNotNull(fAssetAllocation);
        Assertions.assertNotNull(fStockHoldings);
    }

    @Test
    void findFundListOrderByFilter() {
        List<Fund> noFilterFund = fundMapper.findFundListByFilter(null, null, null);
        List<Fund> fundListOrderByFilter = fundMapper.findFundListByFilter(ProductCountry.DOMESTIC, ProductType.EQUITY, new Integer[]{1,2,3,4,5,6});

        Assertions.assertEquals(60, noFilterFund.size());
        System.out.println("fundListOrderByFilter.size() = " + fundListOrderByFilter.size());
        for (Fund fund : fundListOrderByFilter) {
            Assertions.assertEquals(1, fund.getProductRiskGrade());
            Assertions.assertEquals(ProductCountry.DOMESTIC, fund.getFundCountry());
            Assertions.assertEquals(ProductType.EQUITY, fund.getFundType());
            log.info("[findFundListOrderByFilter] Fund : {}", fund);
        }
    }
}