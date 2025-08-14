package com.finsight.backend.service.product.handler;

import com.finsight.backend.common.exception.others.CustomNotValidPathException;
import com.finsight.backend.repository.mapper.DepositMapper;
import com.finsight.backend.domain.vo.product.DepositVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class DepositVoHandler implements ProductVoHandler<DepositVO> {
    private final DepositMapper depositMapper;



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

        Supplier<List<DepositVO>> handler = depositSortHandler().get(sort);
        if(handler == null){
            throw new CustomNotValidPathException();
        }
        return handler.get();
    }

    private Map<String, Supplier<List<DepositVO>>> depositSortHandler() {
        return Map.of(
                "intr_rate", depositMapper::findDepositListOrderByIntrRate,
                "intr_rate2", depositMapper::findDepositListOrderByIntrRate2
        );
    }
}
