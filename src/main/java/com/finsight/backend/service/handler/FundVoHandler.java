package com.finsight.backend.service.handler;

import com.finsight.backend.enumerate.ProductCountry;
import com.finsight.backend.enumerate.ProductType;
import com.finsight.backend.mapper.FundMapper;
import com.finsight.backend.vo.Fund;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FundVoHandler implements ProductVoHandler<Fund> {
    private final FundMapper fundMapper;

    @Override
    public Fund findProduct(String productCode) {
        return fundMapper.findFundByCode(productCode);
    }

    @Override
    public Class<Fund> getProductType() {
        return Fund.class;
    }

    @Override
    public List<Fund> findProductListByFilter(String sort, String country, String type, Integer riskGrade) {
        ProductCountry productCountry = (country == null || country.isBlank())
                ? null
                : ProductCountry.fromDbValue(country);

        ProductType productType = (type == null || type.isBlank())
                ? null
                : ProductType.fromDbValue(type);

        if(sort.equals("fund_scale")){
            // 펀드 규모 정렬해서 리턴
            return fundMapper.findFundListByFilter(productCountry, productType, riskGrade);
        }
        if(sort.equals("rate_of_return")){
            // 수익률 정렬해서 리턴
            return fundMapper.findFundListByFilter(productCountry, productType, riskGrade);
        }
        if(sort.equals("view_count")){
            // 조회수 정렬해서 리턴
            return fundMapper.findFundListByFilter(productCountry, productType, riskGrade);
        }
        throw new RuntimeException("Invalid sort parameter: " + sort);
    }
}
