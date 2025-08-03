package com.finsight.backend.service.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finsight.backend.dto.response.DepositDetailDto;
import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.vo.Deposit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class DepositDtoHandler implements ProductDtoHandler<Deposit>{
    @Override
    public Class<Deposit> getProductType() {
        return Deposit.class;
    }

    @Override
    public ProductDetailDto toDetailDto(Deposit product) {
        String depositOptionRaw = product.getDepositOption();
        List<Map<String, String>> depositOption;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            depositOption = objectMapper.readValue(depositOptionRaw, new TypeReference<>() {});
        } catch (Exception e){
            throw new RuntimeException("자산 구성 정보 파싱 실패 : " + e);
        }
        return DepositDetailDto.depositVoToDepositDetailDto(product, depositOption);
    }

    @Override
    public List<ProductByFilterDto> toFilterDto(List<Deposit> product) {
        
        return null;
    }
}
