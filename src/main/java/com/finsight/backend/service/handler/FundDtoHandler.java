package com.finsight.backend.service.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.dto.response.FundDetailDetailDto;
import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.vo.Fund;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FundDtoHandler implements ProductDtoHandler<Fund> {
    @Override
    public Class<Fund> getProductType() {
        return Fund.class;
    }

    @Override
    public ProductDetailDto toDetailDto(Fund product) {
        String fundAssetAllocationRaw = product.getFundAssetAllocation();
        String fundStockHoldingsRaw = product.getFundStockHoldings();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, String>> fundAssetAllocation;
        List<Map<String, String>> fundStockHoldings;
        try {
            fundAssetAllocation = objectMapper.readValue(fundAssetAllocationRaw, new TypeReference<>() {});
            fundStockHoldings = objectMapper.readValue(fundStockHoldingsRaw, new TypeReference<>() {});
        } catch (Exception e){
            throw new RuntimeException("자산 구성 정보 파싱 실패 : ", e);
        }
        return FundDetailDetailDto.fundVoToFundDetailDto(product, fundAssetAllocation, fundStockHoldings);
    }

    @Override
    public List<ProductByFilterDto> toFilterDto(List<Fund> product) {
        return null;
    }
}
