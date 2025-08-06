package com.finsight.backend.service.handler;

import com.finsight.backend.enumerate.ProductCountry;
import com.finsight.backend.enumerate.ProductType;
import com.finsight.backend.mapper.EtfMapper;
import com.finsight.backend.vo.Etf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EtfVoHandler implements ProductVoHandler<Etf> {
    private final EtfMapper etfMapper;


    @Override
    public Etf findProduct(String productCode) {
        return etfMapper.findEtfByCode(productCode);
    }

    @Override
    public Class<Etf> getProductType() {
        return Etf.class;
    }

    @Override
    public List<Etf> findProductListByFilter(String sort, String country, String type, Integer riskGrade) {
        ProductCountry productCountry = (country == null || country.isBlank())
                ? null
                : ProductCountry.fromDbValue(country);

        ProductType productType = (type == null || type.isBlank())
                ? null
                : ProductType.fromDbValue(type);

        if(sort.equals("volume")){
            // 펀드 규모 정렬해서 리턴
            return etfMapper.findEtfListByFilter(productCountry, productType, riskGrade);
        }
        if(sort.equals("rate_of_return")){
            // 수익률 정렬해서 리턴
            return etfMapper.findEtfListByFilter(productCountry, productType, riskGrade);
        }
        if(sort.equals("view_count")){
            // 조회수 정렬해서 리턴
            return etfMapper.findEtfListByFilter(productCountry, productType, riskGrade);
        }
        throw new RuntimeException("Invalid sort parameter: " + sort);
    }
}
