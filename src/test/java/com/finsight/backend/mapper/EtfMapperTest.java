package com.finsight.backend.mapper;

import com.finsight.backend.config.RootConfig;
import com.finsight.backend.config.WebClientConfig;
import com.finsight.backend.domain.enumerate.ProductCountry;
import com.finsight.backend.domain.enumerate.ProductType;
import com.finsight.backend.repository.mapper.EtfMapper;
import com.finsight.backend.domain.vo.product.EAssetAllocationVO;
import com.finsight.backend.domain.vo.product.EConstituentStocksVO;
import com.finsight.backend.domain.vo.product.EEquityRatioVO;
import com.finsight.backend.domain.vo.product.EtfVO;
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
        WebClientConfig.class
})
class EtfMapperTest {

    @Autowired
    private EtfMapper etfMapper;

    @Test
    void findEtfByCode() {
        EtfVO etf = etfMapper.findEtfByCode("292190");

        Assertions.assertEquals("292190", etf.getProductCode());

        List<EAssetAllocationVO> eAssetAllocation = etf.getEAssetAllocation();
        List<EEquityRatioVO> eEquityRatio = etf.getEEquityRatio();
        List<EConstituentStocksVO> eConstituentStocks = etf.getEConstituentStocks();

        Assertions.assertNotNull(eAssetAllocation);
        Assertions.assertNotNull(eEquityRatio);
        Assertions.assertNotNull(eConstituentStocks);
    }

    @Test
    void findEtfListByFilter() {
        List<EtfVO> noFilterEtf = etfMapper.findEtfListByFilter(null, null, null, 0, 3);
        List<EtfVO> etfListByFilter = etfMapper.findEtfListByFilter(ProductCountry.DOMESTIC, ProductType.EQUITY, new Integer[]{1}, 0, 3);

        Assertions.assertEquals(60, noFilterEtf.size());
        for (EtfVO etf : etfListByFilter) {
            Assertions.assertEquals(1, etf.getProductRiskGrade());
            Assertions.assertEquals(ProductCountry.DOMESTIC, etf.getEtfCountry());
            Assertions.assertEquals(ProductType.EQUITY, etf.getEtfType());
            log.info("[findEtfListByFilter] ETF : {}", etf);
        }
    }
}