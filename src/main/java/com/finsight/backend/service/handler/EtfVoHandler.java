package com.finsight.backend.service.handler;

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
    public List<Etf> findProductListByFilter(String sort, String country, String type, String riskGrade) {
        return null;
    }
}
