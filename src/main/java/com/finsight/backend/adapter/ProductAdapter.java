package com.finsight.backend.adapter;

import com.finsight.backend.vo.Deposit;
import com.finsight.backend.vo.Etf;
import com.finsight.backend.vo.Fund;
import com.finsight.backend.vo.Product;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductAdapter {

    private final Map<String, Class<? extends Product>> productTypeMap;

    public ProductAdapter() {
        this.productTypeMap = Map.of(
                "deposit", Deposit.class,
                "fund", Fund.class,
                "etf", Etf.class
        );
    }

    public Class<? extends Product> productType(String category){
        return productTypeMap.get(category.toLowerCase());
    }
}
