package com.finsight.backend.service.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.dto.response.EtfDetailDetailDto;
import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.vo.Etf;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EtfDtoHandler implements ProductDtoHandler<Etf>{
    @Override
    public Class<Etf> getProductType() {
        return Etf.class;
    }

    @Override
    public ProductDetailDto toDetailDto(Etf product) {
        String etfAssetAllocationRaw = product.getEtfAssetAllocation();
        String etfEquityRatioRaw = product.getEtfEquityRatio();
        String etfConstituentStocksRaw = product.getEtfConstituentStocks();

        List<Map<String, String>> etfAssetAllocation;
        List<Map<String, String>> etfEquityRatio;
        List<Map<String, String>> etfConstituentStocks;
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            etfAssetAllocation = objectMapper.readValue(etfAssetAllocationRaw, new TypeReference<>() {});
            etfEquityRatio = objectMapper.readValue(etfEquityRatioRaw, new TypeReference<>() {});
            etfConstituentStocks = objectMapper.readValue(etfConstituentStocksRaw, new TypeReference<>() {});
        } catch (Exception e){
            throw new RuntimeException("자산 구성 정보 파싱 실패 + " + e);
        }
        return EtfDetailDetailDto.etfVoToEtfDetailDto(product, etfAssetAllocation, etfEquityRatio, etfConstituentStocks);
    }

    @Override
    public List<ProductByFilterDto> toFilterDto(List<Etf> product) {
        return null;
    }
}
