package com.finsight.backend.service.handler;

import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.vo.Deposit;

public class DepositDtoHandler implements ProductDtoHandler<Deposit>{
    @Override
    public Class<Deposit> getProductType() {
        return Deposit.class;
    }

    @Override
    public ProductDetailDto toDetailDto(Deposit product) {
        return null;
    }
}
