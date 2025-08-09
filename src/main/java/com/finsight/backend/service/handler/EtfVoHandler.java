package com.finsight.backend.service.handler;

import com.finsight.backend.enumerate.ProductCountry;
import com.finsight.backend.enumerate.ProductType;
import com.finsight.backend.mapper.EtfMapper;
import com.finsight.backend.mapper.InvTestMapper;
import com.finsight.backend.util.InvUtil;
import com.finsight.backend.vo.Etf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EtfVoHandler implements ProductVoHandler<Etf> {
    private final EtfMapper etfMapper;
    private final InvTestMapper invTestMapper;
    private final InvUtil invUtil;

    @Override
    public Etf findProduct(String productCode) {
        return etfMapper.findEtfByCode(productCode);
    }

    @Override
    public Class<Etf> getProductType() {
        return Etf.class;
    }

    @Override
    public List<Etf> findProductListByFilter(String sort,
                                             String country,
                                             String type,
                                             Boolean isMatched,
                                             String userId) {
        ProductCountry productCountry = (country == null || country.isBlank())
                ? null
                : ProductCountry.fromDbValue(country);

        ProductType productType = (type == null || type.isBlank())
                ? null
                : ProductType.fromDbValue(type);

        String invType = invTestMapper.selectInvestmentProfileTypeByUserId(userId);
        Integer[] riskGradeRange = invUtil.riskGradeRange(invType);
        Integer[] all = new Integer[]{1, 2, 3, 4, 5, 6};

        if(sort.equals("volume")){
            // 펀드 규모 정렬해서 리턴
            return isMatched ?
                    etfMapper.findEtfListByFilter(productCountry, productType, riskGradeRange) :
                    etfMapper.findEtfListByFilter(productCountry, productType, all);
        }
        if(sort.equals("rate_of_return")){
            // 수익률 정렬해서 리턴
            return isMatched ?
                    etfMapper.findEtfListByFilter(productCountry, productType, riskGradeRange) :
                    etfMapper.findEtfListByFilter(productCountry, productType, all);
        }
        if(sort.equals("view_count")){
            // 조회수 정렬해서 리턴
            return isMatched ?
                    etfMapper.findEtfListByFilter(productCountry, productType, riskGradeRange) :
                    etfMapper.findEtfListByFilter(productCountry, productType, all);
        }
        throw new RuntimeException("Invalid sort parameter: " + sort);
    }
}
