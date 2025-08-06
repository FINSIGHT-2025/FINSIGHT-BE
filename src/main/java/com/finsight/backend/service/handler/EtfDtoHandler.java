package com.finsight.backend.service.handler;

import com.finsight.backend.dto.response.EtfByFilterDto;
import com.finsight.backend.dto.response.EtfDetailDetailDto;
import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.vo.Etf;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EtfDtoHandler implements ProductDtoHandler<Etf>{
    @Override
    public Class<Etf> getProductType() {
        return Etf.class;
    }

    @Override
    public ProductDetailDto toDetailDto(Etf product) {
        return EtfDetailDetailDto.etfVoToEtfDetailDto(product);
    }

    @Override
    public List<ProductByFilterDto> toFilterDto(List<Etf> product) {
        return product.stream()
                .map(EtfByFilterDto::etfVoToEtfByFilterDto)
                .collect(Collectors.toList());
    }
}
