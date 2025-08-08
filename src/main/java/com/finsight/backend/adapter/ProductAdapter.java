package com.finsight.backend.adapter;

import com.finsight.backend.vo.Deposit;
import com.finsight.backend.vo.Etf;
import com.finsight.backend.vo.Fund;
import com.finsight.backend.vo.Product;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductAdapter {

    private final Map<String, Class<? extends Product>> categoryMap;

    public ProductAdapter() {
        this.categoryMap = Map.of(
                "deposit", Deposit.class,
                "fund", Fund.class,
                "etf", Etf.class
        );
    }

    public Class<? extends Product> category(String category){
        return categoryMap.get(category.toLowerCase());
    }
}
