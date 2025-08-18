package com.finsight.backend.service.product.handler;

import com.finsight.backend.common.exception.others.CustomNotValidPathException;
import com.finsight.backend.domain.enumerate.ProductCountry;
import com.finsight.backend.domain.enumerate.ProductType;
import com.finsight.backend.repository.mapper.EtfMapper;
import com.finsight.backend.repository.mapper.InvTestMapper;
import com.finsight.backend.common.util.InvUtil;
import com.finsight.backend.domain.vo.product.EtfVO;
import com.finsight.backend.tmptradeserverwebsocket.service.EtfPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EtfVoHandler implements ProductVoHandler<EtfVO> {
    private final EtfMapper etfMapper;
    private final InvTestMapper invTestMapper;
    private final InvUtil invUtil;

    @Override
    public EtfVO findProduct(String productCode) {
        return etfMapper.findEtfByCode(productCode);
    }

    @Override
    public Class<EtfVO> getProductType() {
        return EtfVO.class;
    }

    @Override
    public List<EtfVO> findProductListByFilter(String sort,
                                               String country,
                                               String type,
                                               Boolean isMatched,
                                               String userId,
                                               Integer limit,
                                               Integer offset) {
        ProductCountry productCountry = (country == null || country.isBlank())
                ? null
                : ProductCountry.fromDbValue(country);

        ProductType productType = (type == null || type.isBlank())
                ? null
                : ProductType.fromDbValue(type);

        String invType = invTestMapper.selectInvestmentProfileTypeByUserId(userId);
        Integer[] riskGradeRange = invUtil.riskGradeRange(invType);
        Integer[] all = new Integer[]{1, 2, 3, 4, 5, 6};

        return isMatched ?
                etfMapper.findEtfListByFilter(productCountry, productType, riskGradeRange, limit, offset) :
                etfMapper.findEtfListByFilter(productCountry, productType, all, limit, offset);
    }
}
