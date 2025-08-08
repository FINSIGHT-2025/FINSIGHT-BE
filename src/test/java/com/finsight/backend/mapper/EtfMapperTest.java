package com.finsight.backend.mapper;

import com.finsight.backend.config.CorsConfig;
import com.finsight.backend.config.MongoConfig;
import com.finsight.backend.config.RootConfig;
import com.finsight.backend.config.WebClientConfig;
import com.finsight.backend.enumerate.ProductCountry;
import com.finsight.backend.enumerate.ProductType;
import com.finsight.backend.vo.EAssetAllocation;
import com.finsight.backend.vo.EConstituentStocks;
import com.finsight.backend.vo.EEquityRatio;
import com.finsight.backend.vo.Etf;
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
        WebClientConfig.class
})
class EtfMapperTest {

    @Autowired
    private EtfMapper etfMapper;

    @Test
    void findEtfByCode() {
        Etf etf = etfMapper.findEtfByCode("292190");

        Assertions.assertEquals("292190", etf.getProductCode());

        List<EAssetAllocation> eAssetAllocation = etf.getEAssetAllocation();
        List<EEquityRatio> eEquityRatio = etf.getEEquityRatio();
        List<EConstituentStocks> eConstituentStocks = etf.getEConstituentStocks();

        Assertions.assertNotNull(eAssetAllocation);
        Assertions.assertNotNull(eEquityRatio);
        Assertions.assertNotNull(eConstituentStocks);
    }

    @Test
    void findEtfListByFilter() {
        List<Etf> noFilterEtf = etfMapper.findEtfListByFilter(null, null, null, 0, 3);
        List<Etf> etfListByFilter = etfMapper.findEtfListByFilter(ProductCountry.DOMESTIC, ProductType.EQUITY, 1, 0, 3);

        Assertions.assertEquals(60, noFilterEtf.size());
        for (Etf etf : etfListByFilter) {
            Assertions.assertEquals(1, etf.getProductRiskGrade());
            Assertions.assertEquals(ProductCountry.DOMESTIC, etf.getEtfCountry());
            Assertions.assertEquals(ProductType.EQUITY, etf.getEtfType());
            log.info("[findEtfListByFilter] ETF : {}", etf);
        }
    }
}