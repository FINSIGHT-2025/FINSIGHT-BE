package com.finsight.backend.service.handler;

import com.finsight.backend.dto.response.FundDetailDetailDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.vo.Fund;

public class FundDtoHandler implements ProductDtoHandler<Fund> {
    @Override
    public Class<Fund> getProductType() {
        return Fund.class;
    }

    @Override
    public ProductDetailDto toDetailDto(Fund product) {
        return FundDetailDetailDto.fundVoToFundDetailDto(product, );
    }
}
