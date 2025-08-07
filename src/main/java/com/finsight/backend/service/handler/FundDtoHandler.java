package com.finsight.backend.service.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.dto.response.FundByFilterDto;
import com.finsight.backend.dto.response.FundDetailDetailDto;
import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.vo.Fund;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FundDtoHandler implements ProductDtoHandler<Fund> {
    @Override
    public Class<Fund> getProductType() {
        return Fund.class;
    }

    @Override
    public ProductDetailDto toDetailDto(Fund product) {
        return FundDetailDetailDto.fundVoToFundDetailDto(product);
    }

    @Override
    public List<ProductByFilterDto> toFilterDto(List<Fund> product) {
        return product.stream()
                .map(FundByFilterDto::fundVoToFundByFilterDto)
                .collect(Collectors.toList());
    }
}
