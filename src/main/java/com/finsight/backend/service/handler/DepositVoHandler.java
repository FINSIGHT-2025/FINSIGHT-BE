package com.finsight.backend.service.handler;

import com.finsight.backend.repository.mapper.DepositMapper;
import com.finsight.backend.domain.vo.product.DepositVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class DepositVoHandler implements ProductVoHandler<DepositVO> {
    private final DepositMapper depositMapper;

    private Map<String,  Supplier<List<DepositVO>>> SORT_HANDLERS;


    @Override
    public DepositVO findProduct(String productCode) {
        return depositMapper.findDepositByCode(productCode);
    }

    @Override
    public Class<DepositVO> getProductType() {
        return DepositVO.class;
    }

    @Override
    public List<DepositVO> findProductListByFilter(String sort,
                                                   String country,
                                                   String type,
                                                   Boolean isMatched,
                                                   String userId) {
        SORT_HANDLERS = Map.of(
                "intr_rate", depositMapper::findDepositListOrderByIntrRate,
                "intr_rate2", depositMapper::findDepositListOrderByIntrRate2
        );
        Supplier<List<DepositVO>> handler = SORT_HANDLERS.get(sort);
        if(handler == null){
            throw new RuntimeException("Invalid sort parameter: " + sort);
        }
        return handler.get();
    }
}
