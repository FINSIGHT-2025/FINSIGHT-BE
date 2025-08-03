package com.finsight.backend.service.handler;

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
        return null;
    }
}
