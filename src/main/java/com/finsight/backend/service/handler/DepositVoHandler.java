package com.finsight.backend.service.handler;

import com.finsight.backend.mapper.DepositMapper;
import com.finsight.backend.vo.Deposit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class DepositVoHandler implements ProductVoHandler<Deposit> {
    private final DepositMapper depositMapper;

    private Map<String,  Supplier<List<Deposit>>> SORT_HANDLERS;


    @Override
    public Deposit findProduct(String productCode) {
        return depositMapper.findDepositByCode(productCode);
    }

    @Override
    public Class<Deposit> getProductType() {
        return Deposit.class;
    }

    @Override
    public List<Deposit> findProductListByFilter(String sort,
                                                 String country,
                                                 String type,
                                                 Integer riskGrade) {
        SORT_HANDLERS = Map.of(
                "intr_rate", depositMapper::findDepositListOrderByIntrRate,
                "intr_rate2", depositMapper::findDepositListOrderByIntrRate2
        );
        Supplier<List<Deposit>> handler = SORT_HANDLERS.get(sort);
        if(handler == null){
            throw new RuntimeException("Invalid sort parameter: " + sort);
        }
        return handler.get();
    }
}
