package com.finsight.backend.service;

import com.finsight.backend.config.CorsConfig;
import com.finsight.backend.config.MongoConfig;
import com.finsight.backend.config.RootConfig;
import com.finsight.backend.dto.response.DepositDetailDto;
import com.finsight.backend.dto.response.EtfDetailDetailDto;
import com.finsight.backend.dto.response.FundDetailDetailDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.domain.vo.product.DepositVO;
import com.finsight.backend.domain.vo.product.EtfVO;
import com.finsight.backend.domain.vo.product.FundVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {
        RootConfig.class,
        CorsConfig.class,
        MongoConfig.class
})
class ProductServiceImplTest {

    @Autowired
    private ProductService productService;

    @Test
    void findProduct() {
        String userId = "user";
        ProductDetailDto depositDto = productService.findProduct("1", DepositVO.class, userId);
        ProductDetailDto etfDto = productService.findProduct("292190", EtfVO.class, userId);
        ProductDetailDto fundDto = productService.findProduct("K55301BB5738", FundVO.class, userId);

        Assertions.assertInstanceOf(DepositDetailDto.class, depositDto);
        Assertions.assertInstanceOf(EtfDetailDetailDto.class, etfDto);
        Assertions.assertInstanceOf(FundDetailDetailDto.class, fundDto);

        Assertions.assertEquals("1", depositDto.getProductCode());
        Assertions.assertEquals("292190", etfDto.getProductCode());
        Assertions.assertEquals("K55301BB5738", fundDto.getProductCode());
    }
}