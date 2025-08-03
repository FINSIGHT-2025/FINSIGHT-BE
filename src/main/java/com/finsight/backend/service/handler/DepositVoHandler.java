package com.finsight.backend.service.handler;

import com.finsight.backend.mapper.DepositMapper;
import com.finsight.backend.vo.Deposit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DepositVoHandler implements ProductVoHandler<Deposit> {
    private final DepositMapper depositMapper;


    @Override
    public Deposit findProduct(String productCode) {
        return depositMapper.findDepositByCode(productCode);
    }

    @Override
    public Class<Deposit> getProductType() {
        return Deposit.class;
    }

    @Override
    public List<Deposit> findProductListByFilter(String sort, String country, String type, Integer riskGrade) {

        return null;
    }
}
