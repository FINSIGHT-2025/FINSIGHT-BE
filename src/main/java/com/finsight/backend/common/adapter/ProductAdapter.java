package com.finsight.backend.common.adapter;

import com.finsight.backend.domain.vo.product.DepositVO;
import com.finsight.backend.domain.vo.product.EtfVO;
import com.finsight.backend.domain.vo.product.FundVO;
import com.finsight.backend.domain.vo.product.ProductVO;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductAdapter {

    private final Map<String, Class<? extends ProductVO>> categoryMap;

    public ProductAdapter() {
        this.categoryMap = Map.of(
                "deposit", DepositVO.class,
                "fund", FundVO.class,
                "etf", EtfVO.class
        );
    }

    public Class<? extends ProductVO> category(String category){
        return categoryMap.get(category.toLowerCase());
    }
}
