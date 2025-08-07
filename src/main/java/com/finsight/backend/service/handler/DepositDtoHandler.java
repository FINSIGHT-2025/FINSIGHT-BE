package com.finsight.backend.service.handler;

import com.finsight.backend.dto.response.DepositByFilterDto;
import com.finsight.backend.dto.response.DepositDetailDto;
import com.finsight.backend.dto.response.ProductByFilterDto;
import com.finsight.backend.dto.response.ProductDetailDto;
import com.finsight.backend.vo.DOption;
import com.finsight.backend.vo.Deposit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class DepositDtoHandler implements ProductDtoHandler<Deposit>{
    @Override
    public Class<Deposit> getProductType() {
        return Deposit.class;
    }

    @Override
    public ProductDetailDto toDetailDto(Deposit product) {
        return DepositDetailDto.depositVoToDepositDetailDto(product);
    }

    @Override
    public List<ProductByFilterDto> toFilterDto(List<Deposit> product) {
        return product.stream()
                .map(deposit -> {
                    DOption option = deposit.getDOption().stream()
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("DOption이 비어있습니다: " + deposit.getProductCode()));
                    return DepositByFilterDto.depositVoToDepositByFilterDto(
                            deposit,
                            option.getDOptionIntrRate(),
                            option.getDOptionIntrRate2()
                    );
                })
                .collect(Collectors.toList());
    }
}
